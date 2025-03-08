package com.intern.e_commerce.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.intern.e_commerce.controller.User;
import com.intern.e_commerce.dto.request.AuthenticationRequest;
import com.intern.e_commerce.dto.request.IntrospectRequest;
import com.intern.e_commerce.dto.request.RefreshTokenRequest;
import com.intern.e_commerce.dto.response.IntrospectResponse;
import com.intern.e_commerce.dto.response.RefreshTokenResponse;
import com.intern.e_commerce.entity.InvalidatedToken;
import com.intern.e_commerce.entity.UserEntity;
import com.intern.e_commerce.exception.AppException;
import com.intern.e_commerce.exception.ErrorCode;
import com.intern.e_commerce.repository.InvalidatedTokenRepository;
import com.intern.e_commerce.repository.RoleRepository;
import com.intern.e_commerce.repository.UserRepositoryInterface;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class AuthenticationService {
    final UserRepositoryInterface userRepository;
    final RoleRepository roleRepository;
    final InvalidatedTokenRepository invalidatedTokenRepository;
    private final User user;

    @Value("${jwt.signerKey}")
    @NonFinal
    String signerKey;

    @Value("${jwt.valid-duration}")
    @NonFinal
    int validDuration;

    @Value("${jwt.refreshable-duration}")
    @NonFinal
    int refreshDuration;

    public String authenticateUser(AuthenticationRequest authenticationRequest) {
        log.info(signerKey);
        UserEntity userEntity = userRepository
                .findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), userEntity.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.PASSWORD_INVALID);
        }
        return generateToken(userEntity);
    }

    public IntrospectResponse introspectUser(IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        var token = introspectRequest.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (Exception e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    public void logout(String token) throws ParseException, JOSEException {
        var verifiedToken = verifyToken(token, true);
        var invalidatedToken = InvalidatedToken.builder()
                .id(verifiedToken.getJWTClaimsSet().getJWTID())
                .expiryTime(verifiedToken.getJWTClaimsSet().getExpirationTime())
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {

        var verifiedToken = verifyToken(request.getToken(), true);
        var invalidatedToken = InvalidatedToken.builder()
                .id(verifiedToken.getJWTClaimsSet().getJWTID())
                .expiryTime(verifiedToken.getJWTClaimsSet().getExpirationTime())
                .build();
        invalidatedTokenRepository.save(invalidatedToken);

        return RefreshTokenResponse.builder()
                .token(generateToken(userRepository
                        .findByUsername(verifiedToken.getJWTClaimsSet().getSubject())
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED))))
                .build();
    }

    SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationTime = isRefresh
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(refreshDuration, ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        if (!(expirationTime.after(new Date()) && signedJWT.verify(verifier))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    String generateToken(UserEntity user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer(".com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(validDuration, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("scope", buildScope(user))
                .jwtID(UUID.randomUUID().toString())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    String buildScope(UserEntity user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!user.getRoles().isEmpty()) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });
        }
        return stringJoiner.toString();
    }
}

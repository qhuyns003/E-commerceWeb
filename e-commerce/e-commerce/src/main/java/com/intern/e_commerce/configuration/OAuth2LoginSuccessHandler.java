package com.intern.e_commerce.configuration;

import com.intern.e_commerce.entity.Cart;
import com.intern.e_commerce.entity.Role;
import com.intern.e_commerce.entity.UserEntity;
import com.intern.e_commerce.enums.AuthProvider;
import com.intern.e_commerce.exception.AppException;
import com.intern.e_commerce.exception.ErrorCode;
import com.intern.e_commerce.repository.RoleRepository;
import com.intern.e_commerce.repository.UserRepositoryInterface;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    RoleRepository roleRepository;
    UserRepositoryInterface userRepository;

    @Value("${jwt.signerKey}")
    @NonFinal
    String signerKey;

    @Value("${jwt.valid-duration}")
    @NonFinal
    int validDuration;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        String provider = getAuthProvider(request, oauthUser);
        AuthProvider authProviderEnum = AuthProvider.valueOf(provider.toUpperCase());
        String socialId;
        if (authProviderEnum == AuthProvider.GOOGLE) {
            socialId = oauthUser.getAttribute("sub").toString();
        } else if (authProviderEnum == AuthProvider.FACEBOOK) {
            socialId = oauthUser.getAttribute("id").toString();
        } else {
            socialId = null;
        }


        UserEntity userEntity = userRepository.findBySocialIdAndAndAuthProvider(socialId,authProviderEnum)
                .orElseGet(() -> {
                    Set<Role> roles = new HashSet<>();
                    roles.add(roleRepository.findById("USER")
                            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)));

                    UserEntity user=UserEntity.builder()
                            .email(email)
                            .username(socialId)
                            .roles(roles)
                            .authProvider(authProviderEnum)
                            .socialId(socialId)
                            .build();
                    user.setCart(Cart.builder()
                            .user(user)
                            .build());
                    return userRepository.save(user);
                });

        String token = generateToken(userEntity);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"accessToken\": \"" + token + "\"}");
    }

    private String getAuthProvider(HttpServletRequest request, OAuth2User oauthUser) {
        System.out.println("OAuth2 Attributes: " + oauthUser.getAttributes());

        String id = oauthUser.getAttribute("id");
        if (id != null) {
            return "FACEBOOK";
        }

        String sub = oauthUser.getAttribute("sub");
        if (sub != null && sub.matches("^\\d+$")) { // Google sub thường là số
            return "GOOGLE";
        }

        return "LOCAL"; // Mặc định
    }



    String generateToken(UserEntity user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer(".com")
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(validDuration, ChronoUnit.SECONDS)))
                .claim("scope", buildScope(user))
                .jwtID(UUID.randomUUID().toString())
                .build();

        try {
            JWSObject jwsObject = new JWSObject(header, new Payload(jwtClaimsSet.toJSONObject()));
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    String buildScope(UserEntity user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }
        return stringJoiner.toString();
    }
}

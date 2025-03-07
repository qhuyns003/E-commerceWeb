package com.intern.e_commerce.exception;

import java.util.Map;
import java.util.Objects;

import jakarta.validation.ConstraintViolation;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.intern.e_commerce.configuration.Translator;
import com.intern.e_commerce.dto.response.ApiResponse;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@Hidden
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<Object>> handleRuntimeException(Exception e) {
        log.error(e.getMessage(), e);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getHttpStatus())
                .body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<Object>> handleAppException(AppException e) {
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(e.getErrorCode().getCode())
                .message(Translator.toLocale(e.getErrorCode().getMessage()))
                .build();
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(apiResponse);
    }

    @ExceptionHandler(value = AuthorizationDeniedException.class)
    ResponseEntity<ApiResponse<Object>> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(ErrorCode.ACCESS_DENIED.getCode())
                .message(ErrorCode.ACCESS_DENIED.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.ACCESS_DENIED.getHttpStatus()).body(apiResponse);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(ErrorCode.DATETIME_INVALID.getCode())
                .message(ErrorCode.DATETIME_INVALID.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.DATETIME_INVALID.getHttpStatus()).body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Object>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String enumKey =
                Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
            var constraintViolations =
                    e.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);
            attributes = constraintViolations.getConstraintDescriptor().getAttributes();

        } catch (Exception ex) {
            log.info("invalid key");
        }
        apiResponse.setMessage(
                Objects.nonNull(attributes)
                        ? mapAtrributes(errorCode.getMessage(), attributes)
                        : errorCode.getMessage());
        apiResponse.setCode(errorCode.getCode());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(apiResponse);
    }

    String mapAtrributes(String message, Map<String, Object> attributes) {
        message = message.replace(
                "{" + MIN_ATTRIBUTE + "}", attributes.get(MIN_ATTRIBUTE).toString());
        return message;
    }
}

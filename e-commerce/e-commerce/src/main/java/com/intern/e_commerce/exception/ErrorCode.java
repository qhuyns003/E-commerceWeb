package com.intern.e_commerce.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.BAD_GATEWAY),
    INVALID_KEY(1001, "Invalid enum key", HttpStatus.BAD_GATEWAY),
    USER_EXISTED(1002, "User already existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username is invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password is invalid", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User is not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(1007, "Access denied", HttpStatus.FORBIDDEN),
    ROLE_NOT_FOUND(1008, "Role not found", HttpStatus.BAD_GATEWAY),
    ROLE_EXISTED(1009, "Role already existed", HttpStatus.BAD_GATEWAY),
    DOB_INVALID(1010, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    DATETIME_INVALID(1011, "Your date is invalid", HttpStatus.BAD_REQUEST);
    private Integer code;
    private String message;
    private HttpStatus httpStatu

    private ErrorCode(Integer code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

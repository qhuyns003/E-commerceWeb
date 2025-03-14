package com.intern.e_commerce.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

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
    DATETIME_INVALID(1011, "Your date is invalid", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND(1012, "Product not found", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(1013, "Order not found", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED(1014, "Category already existed", HttpStatus.BAD_GATEWAY),
    CATEGORY_NOT_EXISTED(1015, "Category is not existed", HttpStatus.BAD_GATEWAY),
    PASSWORD_WRONG(1016, "You must check your password again", HttpStatus.BAD_REQUEST),
    PRODUCT_EXISTED(1017, "Product already existed", HttpStatus.BAD_REQUEST);

    private Integer code;
    private String message;
    private HttpStatus httpStatus;

    private ErrorCode(Integer code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

package com.foodapp.constants;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    // Unauthorized error:
    // Use case: Whenever Http Status code equals 401 (Unauthorized) -> force logout current user
    TOKEN_EXPIRED(1000, "Token Expired", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(1001, "Token Invalid", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1002, "Unauthorized", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN(1003, "Invalid Refresh Token", HttpStatus.UNAUTHORIZED),
    ACCOUNT_DISABLED(1004, "Account Disabled", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(1005, "Access Denied", HttpStatus.UNAUTHORIZED),
    // Usual error:
    USERNAME_OR_PASSWORD_INCORRECT(1004, "Username or Password Incorrect", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1005, "User Not Found", HttpStatus.NOT_FOUND),
    USED_PHONE(1006, "Phone already used", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1007, "Role Not Found", HttpStatus.NOT_FOUND),
    INVALID_REQUEST(1008, "Invalid Request", HttpStatus.BAD_REQUEST),

    // --- ORDER ERRORS ---
    ORDER_NOT_FOUND(2001, "Order not found", HttpStatus.NOT_FOUND),

    INVALID_ORDER_STATUS(2002, "Invalid order status", HttpStatus.NOT_FOUND),
    // --- FOOD ERRORS ---
    FOOD_NOT_FOUND(3001, "Food not found", HttpStatus.NOT_FOUND),

    // --- CATEGORY ERRORS ---
    CATEGORY_NOT_FOUND(1009, "Category Not found", HttpStatus.NOT_FOUND),

    ORDER_DETAIL_NOT_FOUND(1010, "Order Detail Not found", HttpStatus.NOT_FOUND),

    // Unexpected error:
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception", HttpStatus.BAD_REQUEST),
    ;
    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}

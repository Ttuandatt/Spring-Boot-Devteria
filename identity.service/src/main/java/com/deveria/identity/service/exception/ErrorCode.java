package com.deveria.identity.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid key", HttpStatus.BAD_REQUEST),   // Thêm mã lỗi cho khóa không hợp lệ
    USER_EXIST(1002, "User already exists", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(1003, "Username must be between 3 and 20 characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least 5 characters long", HttpStatus.BAD_REQUEST),
    USER_NOT_EXIST(1005, "User not exists", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED), // Error code: 401
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN) // Error code: 403
    ;
    private int code;
    private String message;
    private HttpStatusCode statusCode;


    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

}

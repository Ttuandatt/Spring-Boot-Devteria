package com.deveria.identity.service.exception;

public enum ErrorCode {
    UNCATEGORIZED(9999, "Uncategorized error"),
    INVALID_KEY(1001, "Invalid key"),   // Thêm mã lỗi cho khóa không hợp lệ
    USER_EXIST(1002, "User already exists"),
    INVALID_USERNAME(1003, "Username must be between 3 and 20 characters"),
    INVALID_PASSWORD(1004, "Password must be at least 6 characters long")
    ;
    private int code;
    private String message;


    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

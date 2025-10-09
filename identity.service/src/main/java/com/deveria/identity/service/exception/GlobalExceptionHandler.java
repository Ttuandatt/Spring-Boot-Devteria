package com.deveria.identity.service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Bắt tất cả các RuntimeException và trả về mã lỗi 400 (Bad Request)
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    // Bắt lỗi validate dữ liệu đầu vào và trả về mã lỗi 400 (Bad Request)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<String> handleValidationException(MethodArgumentNotValidException e){
        return ResponseEntity.badRequest().body(e.getFieldError().getDefaultMessage());
    }

}

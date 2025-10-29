package com.deveria.identity.service.exception;

import com.deveria.identity.service.dto.response.ApiResponse;
import com.deveria.identity.service.util.LogUtils;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.access.AccessDeniedException;

import java.util.Map;
import java.util.Objects;


@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";

    // Bắt tất cả các RuntimeException và trả về mã lỗi 400 (Bad Request)
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e) {
        ApiResponse response = new ApiResponse();

        response.setCode(ErrorCode.UNCATEGORIZED.getCode());
        response.setMessage(ErrorCode.UNCATEGORIZED.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    // Bắt lỗi AppException và trả về mã lỗi 400 (Bad Request)
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse response = new ApiResponse();

        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(response);
    }

    // Bắt lỗi validate dữ liệu đầu vào và trả về mã lỗi 400 (Bad Request)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException e){
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;
        try{
            errorCode = ErrorCode.valueOf(enumKey);

            var constraintViolation = e.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class); // Lấy ra ConstraintViolation từ lỗi

            attributes = constraintViolation.getConstraintDescriptor().getAttributes(); // Lấy ra các thuộc tính của annotation

            LogUtils.logMethodInfo(attributes.toString()); // Log các thuộc tính để kiểm tra

        } catch (IllegalArgumentException ex){
            // Nếu không tìm thấy mã lỗi tương ứng, giữ nguyên errorCode là INVALID_KEY
        }

        ApiResponse response = new ApiResponse();

        response.setCode(errorCode.getCode());
        response.setMessage(Objects.nonNull(attributes) ?
                mapAttribute(errorCode.getMessage(), attributes) : errorCode.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }

    // Bắt lỗi AccessDeniedException và trả về mã lỗi 403 (Forbidden)
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException e){
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

}

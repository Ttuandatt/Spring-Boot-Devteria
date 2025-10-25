package com.deveria.identity.service.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.*;

//@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE}) // Xác định phạm vi mà annotation này có thể áp dụng
@Target({ElementType.FIELD})    // Trong trường hợp này, chỉ áp dụng cho các trường (fields)
@Retention(RetentionPolicy.RUNTIME) // Xác định thời gian tồn tại của annotation này (tại runtime)
@Constraint(validatedBy = {DobValidator.class}) // Chỉ định các lớp validator sẽ xử lý logic kiểm tra
public @interface DobConstraint {
    String message() default "Invalid date of birth"; // Thông điệp lỗi mặc định khi vi phạm ràng buộc

    Class<?>[] groups() default {}; // Nhóm ràng buộc, thường dùng để phân loại các ràng buộc

    Class<? extends Payload>[] payload() default {}; // Thông tin bổ sung về ràng buộc, thường dùng để truyền metadata

    // Property customize để chỉ định thuộc tính nào sẽ được kiểm tra
    int min(); // Tuổi tối thiểu
}

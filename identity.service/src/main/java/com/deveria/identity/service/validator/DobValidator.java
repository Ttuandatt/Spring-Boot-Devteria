package com.deveria.identity.service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

    private int min;

    // Method để kiểm tra tính hợp lệ của ngày tháng năm sinh
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if(Objects.isNull(value))
            return true; // Cho phép giá trị null, nếu muốn bắt buộc phải có giá trị thì thêm @NotNull vào trường đó

        long years = ChronoUnit.YEARS.between(value, LocalDate.now()); // Tính số năm giữa ngày sinh và ngày hiện tại

        return years >= min;
    }

    // Method khởi tạo validator, có thể sử dụng để lấy các thuộc tính từ annotation nếu cần
    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min(); // Lấy giá trị min từ annotation
    }
}

//<DobConstraint, LocalDate>: DobConstraint là annotation tùy chỉnh mà validator này sẽ xử lý, và LocalDate là kiểu dữ liệu mà annotation này sẽ được áp dụng (trong trường hợp này là ngày tháng năm sinh).

package com.deveria.identity.service.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // Chỉ bao gồm các trường không null trong JSON. Nếu trường nào null thì sẽ không xuất hiện trong JSON.
public class ApiResponse<T> {
    int code = 1000; // 1000: success, other: error
    String message;
    T result;

}

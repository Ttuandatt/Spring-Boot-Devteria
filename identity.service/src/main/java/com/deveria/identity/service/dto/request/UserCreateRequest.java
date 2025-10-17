package com.deveria.identity.service.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    @Size(min = 3, max = 20, message = "INVALID_USERNAME")
    String username;
    @Size(min = 5,  message = "INVALID_PASSWORD")
    String password;
    String firstName;
    String lastName;
    LocalDate dob;

    // @Data generates getters, setters, toString, equals, and hashCode methods
    // No need to manually write them
    // If you need custom logic, you can still define them manually

    // @Builder provides a builder pattern for easy object creation. It can be used as follows:
    // UserCreateRequest request = UserCreateRequest.builder().username("user1").password("pass123").build();

    // @NoArgsConstructor generates a no-argument constructor
    // @AllArgsConstructor generates a constructor with all fields as parameters

    // @FieldDefaults(level = AccessLevel.PRIVATE) makes all fields private by default
}

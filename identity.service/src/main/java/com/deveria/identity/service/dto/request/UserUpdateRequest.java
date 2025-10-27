package com.deveria.identity.service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String username;
    String password;
    String firstName;
    String lastName;
    LocalDate dob;
    List<String> roles;

    // @Data generates getters, setters, toString, equals, and hashCode methods
    // No need to manually write them
    // If you need custom logic, you can still define them manually

    // @Builder provides a builder pattern for easy object creation. It can be used as follows:
    // UserUpdateRequest request = UserUpdateRequest.builder().username("user1").password("pass123").build();

    // @NoArgsConstructor generates a no-argument constructor
    // @AllArgsConstructor generates a constructor with all fields as parameters

    // @FieldDefaults(level = AccessLevel.PRIVATE) makes all fields private by default
}

package com.deveria.identity.service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob; //yyyy-mm-dd
}

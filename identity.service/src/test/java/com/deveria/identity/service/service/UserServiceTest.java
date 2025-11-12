package com.deveria.identity.service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.deveria.identity.service.dto.request.UserCreateRequest;
import com.deveria.identity.service.dto.response.UserResponse;
import com.deveria.identity.service.entity.User;
import com.deveria.identity.service.repository.UserRepository;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    private UserCreateRequest request;
    private UserResponse response;
    private User user;
    private LocalDate dob;

    // Init test data before each test
    @BeforeEach
    void initData() {
        dob = LocalDate.of(2000, 5, 4);
        request = UserCreateRequest.builder()
                .username("test4")
                .password("12345")
                .firstName("cu")
                .lastName("loz")
                .dob(dob)
                .build();

        response = UserResponse.builder()
                .id("79161462-a2f0-4581-8e03-b8e6b9cc0c8d")
                .username("test4")
                .firstName("cu")
                .lastName("loz")
                .dob(dob)
                .build();

        user = User.builder()
                .id(response.getId())
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dob(request.getDob())
                .build();
    }

    @Test
    void createUser_validRequest_Success() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var response = userService.createUser(request);

        // THEN
        assertThat(response.getId()).isEqualTo("79161462-a2f0-4581-8e03-b8e6b9cc0c8d");
        assertThat(response.getUsername()).isEqualTo("test4");
    }
}

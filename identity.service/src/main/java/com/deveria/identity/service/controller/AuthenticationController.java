package com.deveria.identity.service.controller;

import com.deveria.identity.service.dto.request.AuthenticationRequest;
import com.deveria.identity.service.dto.request.IntrospectRequest;
import com.deveria.identity.service.dto.response.ApiResponse;
import com.deveria.identity.service.dto.response.AuthenticationResponse;
import com.deveria.identity.service.dto.response.IntrospectResponse;
import com.deveria.identity.service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
//    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
//        // Gọi service để xác thực người dùng
//        var result = authenticationService.authenticate(request);
//
//        // Trả về kết quả trong ApiResponse
//        return ApiResponse.<AuthenticationResponse>builder()
//                .result(result)
//                .build();
//    }
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        var result = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        // Gọi service để xác thực người dùng
        var result = authenticationService.introspect(request);

        // Trả về kết quả trong ApiResponse
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

}

package com.deveria.identity.service.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Các endpoint công khai không yêu cầu xác thực
    private final String[] PUBLIC_ENDPOINTS = {
        "/users", "/auth/token", "/auth/introspect", "/auth/logout", "/auth/refresh"
    };

    @Autowired
    private CustomJwtDecoder customJwtDecoder; // Sử dụng CustomJwtDecoder để giải mã và xác thực token JWT

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Cấu hình cho phép truy cập công khai đến các endpoint nhất định
        httpSecurity.authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS)
                .permitAll()
                //                                .requestMatchers(HttpMethod.GET, "/users").hasAuthority("SCOPE_ADMIN")
                // // Chỉ cho phép user với vai trò ADMIN truy cập /users
                .anyRequest()
                .authenticated());

        // Cấu hình để sử dụng JWT làm phương thức xác thực cho resource server. Ví dụ: khi có yêu cầu đến các endpoint
        // bảo vệ, server sẽ kiểm tra token JWT trong header Authorization.
        // Để cấu hình này hoạt động, ta cần cung cấp một JwtDecoder để giải mã và xác thực token JWT.
        // Ví dụ: ta không cho /users công khai, thì muốn access được ta phải gửi kèm token JWT trong header
        // Authorization.
        httpSecurity.oauth2ResourceServer(
                oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(
                                new JwtAuthenticationEntryPoint()) // Cấu hình điểm vào xác thực (authentication entry
                // point) để xử lý các yêu cầu không xác thực. Ví dụ
                // với lỗi Error 401 thì nó được xử lý ở phần filter
                // trước khi vào đến GlobalExceptionHandler, nên cần
                // làm cái này để GlobalExceptionHandler bắt được lỗi
                // 401. Và method này yêu cầu phải imlement
                // AuthenticationEntryPoint nên ta tạo 1 class
                // JwtAuthenticationEntryPoint implements
                // AuthenticationEntryPoint.
                // oauth2.jwt(jwtConfigurer ->
                // jwtConfigurer.decoder(jwtDecoder()).jwtAuthenticationConverter(jwtAuthenticationConverter())) nếu
                // muốn custom mapping authorities
                );

        // Tác dụng: Vô hiệu hóa CSRF để tránh lỗi 403 khi gửi yêu cầu từ Postman hoặc trình duyệt
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    // Cấu hình để ánh xạ các quyền (authorities) từ token JWT.
    // Mặc định, Spring Security không tự động ánh xạ các quyền từ token JWT,
    // nên ta cần cấu hình để nó hiểu các quyền này.
    // Trong ví dụ này, ta cấu hình để các quyền trong token JWT được ánh xạ thành các vai trò (roles) trong Spring
    // Security.
    // Cụ thể, ta thêm tiền tố "ROLE_" vào trước mỗi quyền để Spring Security nhận diện chúng như các vai trò.
    // Mặc định các quyền có tiền tố là "SCOPE_", nên nếu token JWT có quyền "SCOPE_ADMIN",
    // thì sau khi ánh xạ sẽ thành "ROLE_ADMIN", và ta có thể sử dụng "hasRole("ADMIN")" trong cấu hình bảo mật.
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}

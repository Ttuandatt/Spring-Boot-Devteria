package com.deveria.identity.service.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Các endpoint công khai không yêu cầu xác thực
    private final String[] PUBLIC_ENDPOINTS = {"/users", "/auth/token", "/auth/introspect"};

    @Value("${jwt.signer.key}") // Lấy giá trị từ file cấu hình application.properties
    private String signerKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Cấu hình cho phép truy cập công khai đến các endpoint nhất định
        httpSecurity.authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest()
                        .authenticated()
                );


        // Cấu hình để sử dụng JWT làm phương thức xác thực cho resource server. Ví dụ: khi có yêu cầu đến các endpoint bảo vệ, server sẽ kiểm tra token JWT trong header Authorization.
        // Để cấu hình này hoạt động, ta cần cung cấp một JwtDecoder để giải mã và xác thực token JWT.
        // Ví dụ: ta không cho /users công khai, thì muốn access được ta phải gửi kèm token JWT trong header Authorization.
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
        );


        // Tác dụng: Vô hiệu hóa CSRF để tránh lỗi 403 khi gửi yêu cầu từ Postman hoặc trình duyệt
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS256");

        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

}

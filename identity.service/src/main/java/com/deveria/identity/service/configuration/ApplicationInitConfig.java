package com.deveria.identity.service.configuration;

import com.deveria.identity.service.entity.User;
import com.deveria.identity.service.enums.Role;
import com.deveria.identity.service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j // Thêm annotation để hỗ trợ logging
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    // Method này sẽ chạy khi ứng dụng khởi động
    // Có tác dụng kiểm tra nếu chưa có user "admin" thì tạo mới user này với password "admin"
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if(!userRepository.findByUsername("admin").isPresent()){
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.name());

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
//                        .roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("admin user created with username: admin and password: admin");
            }
        };
    }
}

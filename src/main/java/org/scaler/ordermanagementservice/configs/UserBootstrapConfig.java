package org.scaler.ordermanagementservice.configs;

import org.scaler.ordermanagementservice.models.User;
import org.scaler.ordermanagementservice.repositories.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class UserBootstrapConfig {

    @Bean
    ApplicationRunner bootstrapAdminUser(
            Environment environment,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            String username = environment.getProperty("APP_SECURITY_ADMIN_USERNAME");
            String password = environment.getProperty("APP_SECURITY_ADMIN_PASSWORD");

            if (username == null || username.isBlank() || password == null || password.isBlank()) {
                return;
            }

            if (userRepository.findByUsername(username).isEmpty()) {
                userRepository.save(new User(username, passwordEncoder.encode(password), List.of("ADMIN")));
            }
        };
    }
}

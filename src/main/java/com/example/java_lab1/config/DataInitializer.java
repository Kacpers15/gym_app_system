package com.example.java_lab1.config;

import com.example.java_lab1.model.Role;
import com.example.java_lab1.model.User;
import com.example.java_lab1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.email}") String email,
            @Value("${app.admin.password}") String password,
            @Value("${app.admin.first-name}") String firstName,
            @Value("${app.admin.last-name}") String lastName) {
        return args -> {
            if (!userRepository.existsByEmail(email)) {
                User admin = new User();
                admin.setFirstName(firstName);
                admin.setLastName(lastName);
                admin.setEmail(email.toLowerCase());
                admin.setPassword(passwordEncoder.encode(password));
                admin.setRole(Role.ADMIN);
                admin.setEnabled(true);
                userRepository.save(admin);
            }
        };
    }

}
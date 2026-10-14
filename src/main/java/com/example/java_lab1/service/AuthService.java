package com.example.java_lab1.service;

import com.example.java_lab1.dto.AuthResponse;
import com.example.java_lab1.dto.LoginRequest;
import com.example.java_lab1.dto.RegisterRequest;
import com.example.java_lab1.model.Role;
import com.example.java_lab1.model.User;
import com.example.java_lab1.repository.UserRepository;
import com.example.java_lab1.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER); // nigdy z requestu
        user.setEnabled(true);

        userRepository.save(user);
        return new AuthResponse(
                jwtService.generateToken(user.getEmail(), user.getRole()),
                user.getEmail(),
                user.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.password()));

        User user = userRepository.findByEmail(email).orElseThrow();
        return new AuthResponse(
                jwtService.generateToken(user.getEmail(), user.getRole()),
                user.getEmail(),
                user.getRole());
    }
}
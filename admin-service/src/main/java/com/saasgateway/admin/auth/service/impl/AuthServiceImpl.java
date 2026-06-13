package com.saasgateway.admin.auth.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.saasgateway.admin.auth.dto.CurrentUserResponse;
import com.saasgateway.admin.auth.dto.LoginRequest;
import com.saasgateway.admin.auth.dto.LoginResponse;
import com.saasgateway.admin.auth.security.JwtService;
import com.saasgateway.admin.auth.service.AuthService;
import com.saasgateway.admin.user.entity.AdminUser;
import com.saasgateway.admin.user.entity.AdminUserStatus;
import com.saasgateway.admin.user.repository.AdminUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    @Override
    public LoginResponse login(LoginRequest request) {
        AdminUser user = adminUserRepository.findByEmail(request.email())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Invalid credentials"
                                )
                        );

        if (user.getStatus() != AdminUserStatus.ACTIVE) {
            throw new RuntimeException(
                    "User disabled"
            );
        }
        boolean valid = passwordEncoder.matches(request.password(), user.getPasswordHash());
        if (!valid) {
            throw new RuntimeException(
                    "Invalid credentials"
            );
        }
        String token = jwtService.generateToken(user);
        return new LoginResponse(token, user.getEmail(), user.getRole().name());
    }

    @Override
    public CurrentUserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        AdminUser user = adminUserRepository.findByEmail(email).orElseThrow();
        return new CurrentUserResponse(user.getEmail(), user.getRole().name());
    }

}
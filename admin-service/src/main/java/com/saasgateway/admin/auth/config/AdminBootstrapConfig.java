package com.saasgateway.admin.auth.config;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.saasgateway.admin.user.entity.AdminRole;
import com.saasgateway.admin.user.entity.AdminUser;
import com.saasgateway.admin.user.entity.AdminUserStatus;
import com.saasgateway.admin.user.repository.AdminUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminBootstrapConfig {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void bootstrapAdminUser() {
        createUserIfMissing("superadmin@saasgateway.com", AdminRole.SUPER_ADMIN);
        createUserIfMissing("admin@saasgateway.com", AdminRole.ADMIN);
        createUserIfMissing("viewer@saasgateway.com", AdminRole.VIEWER);
    }

    private void createUserIfMissing(String email, AdminRole role) {
        if (adminUserRepository.findByEmail(email).isPresent()) {
            return;
        }
        AdminUser user = AdminUser.builder()
                .id(UUID.randomUUID())
                .email(email)
                .passwordHash(passwordEncoder.encode("admin123"))
                .role(role)
                .status(AdminUserStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        adminUserRepository.save(user);
        log.info("Created user {}", email);
    }
}
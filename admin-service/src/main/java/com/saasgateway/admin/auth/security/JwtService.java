package com.saasgateway.admin.auth.security;

import com.saasgateway.admin.user.entity.AdminUser;

public interface JwtService {
    String generateToken(AdminUser user);
    String extractEmail(String token);
    boolean isValid(String token);
}
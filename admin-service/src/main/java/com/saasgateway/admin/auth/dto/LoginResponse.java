package com.saasgateway.admin.auth.dto;

public record LoginResponse(
        String token,
        String email,
        String role
) {
}
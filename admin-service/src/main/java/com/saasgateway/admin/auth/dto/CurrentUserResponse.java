package com.saasgateway.admin.auth.dto;

public record CurrentUserResponse(
        String email,
        String role
) {
}
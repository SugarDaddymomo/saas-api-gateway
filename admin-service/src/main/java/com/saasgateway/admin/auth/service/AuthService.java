package com.saasgateway.admin.auth.service;

import com.saasgateway.admin.auth.dto.CurrentUserResponse;
import com.saasgateway.admin.auth.dto.LoginRequest;
import com.saasgateway.admin.auth.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    CurrentUserResponse getCurrentUser();
}
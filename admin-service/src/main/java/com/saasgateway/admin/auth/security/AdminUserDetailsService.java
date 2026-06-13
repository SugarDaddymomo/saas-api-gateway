package com.saasgateway.admin.auth.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.saasgateway.admin.user.entity.AdminUser;
import com.saasgateway.admin.user.repository.AdminUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminUserRepository adminUserRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AdminUser user = adminUserRepository.findByEmail(email).orElseThrow();
        return User.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
                .roles(user.getRole().name())
                .build();
    }

}
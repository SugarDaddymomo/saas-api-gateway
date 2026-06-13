package com.saasgateway.admin.user.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.saasgateway.admin.user.entity.AdminUser;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, UUID>{

    Optional<AdminUser> findByEmail(String email);
}
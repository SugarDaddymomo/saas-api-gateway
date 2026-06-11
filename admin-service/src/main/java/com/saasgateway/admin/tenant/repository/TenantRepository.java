package com.saasgateway.admin.tenant.repository;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.saasgateway.admin.tenant.entity.Tenant;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, UUID>, JpaSpecificationExecutor<Tenant>{
    Page<Tenant> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
package com.saasgateway.gateway_service.api_key.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.saasgateway.gateway_service.api_key.entity.ApiKey;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID>, JpaSpecificationExecutor<ApiKey>{

    Page<ApiKey> findByTenantId(UUID tenantId, Pageable pageable);

    Optional<ApiKey> findByKeyHash(String keyHash);

    @Query("""
        SELECT ak
        FROM ApiKey ak
        JOIN FETCH ak.tenant
        WHERE ak.keyHash = :keyHash
    """)
    Optional<ApiKey> findByKeyHashWithTenant(String keyHash);
}
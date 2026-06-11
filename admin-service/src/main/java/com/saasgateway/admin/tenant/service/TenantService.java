package com.saasgateway.admin.tenant.service;

import java.util.UUID;
import org.springframework.data.domain.Page;
import com.saasgateway.admin.tenant.dto.CreateTenantRequest;
import com.saasgateway.admin.tenant.dto.CreateTenantResponse;
import com.saasgateway.admin.tenant.dto.MessageResponse;
import com.saasgateway.admin.tenant.dto.TenantResponse;
import com.saasgateway.admin.tenant.dto.TenantSearchRequest;

public interface TenantService {

    CreateTenantResponse createTenant(CreateTenantRequest request);
    Page<TenantResponse> getTenants(TenantSearchRequest request);
    TenantResponse getTenant(UUID tenantId);
    TenantResponse updateTenant(UUID tenantId, CreateTenantRequest request);
    MessageResponse disableTenant(UUID tenantId);
    MessageResponse enableTenant(UUID tenantId);
}
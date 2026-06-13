package com.saasgateway.admin.tenant.dto;

import java.util.UUID;
import com.saasgateway.admin.tenant.entity.TenantPlan;
import com.saasgateway.admin.tenant.entity.TenantStatus;

public record CreateTenantResponse(
        UUID id,
        String name,
        TenantStatus status,
        TenantPlan plan,
        Integer rateLimit    
) 
{

}

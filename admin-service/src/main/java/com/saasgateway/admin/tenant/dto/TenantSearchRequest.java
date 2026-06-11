package com.saasgateway.admin.tenant.dto;

import com.saasgateway.admin.tenant.entity.TenantPlan;
import com.saasgateway.admin.tenant.entity.TenantStatus;

public record TenantSearchRequest(
        String search,
        TenantStatus status,
        TenantPlan plan,
        String sortBy,
        String sortDirection,
        Integer page,
        Integer size
) {
}
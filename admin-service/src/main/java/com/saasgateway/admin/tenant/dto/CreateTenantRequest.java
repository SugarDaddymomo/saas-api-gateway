package com.saasgateway.admin.tenant.dto;

import com.saasgateway.admin.tenant.entity.TenantPlan;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTenantRequest(

    @NotBlank
    @Size(max = 255)
    String name,

    @NotNull
    TenantPlan plan,

    @NotNull
    Integer rateLimit
) {

}

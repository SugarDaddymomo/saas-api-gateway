package com.saasgateway.admin.tenant.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.saasgateway.admin.tenant.dto.CreateTenantRequest;
import com.saasgateway.admin.tenant.dto.CreateTenantResponse;
import com.saasgateway.admin.tenant.dto.MessageResponse;
import com.saasgateway.admin.tenant.dto.TenantResponse;
import com.saasgateway.admin.tenant.dto.TenantSearchRequest;
import com.saasgateway.admin.tenant.entity.TenantPlan;
import com.saasgateway.admin.tenant.entity.TenantStatus;
import com.saasgateway.admin.tenant.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateTenantResponse createTenant(@Valid @RequestBody CreateTenantRequest request) {
        return tenantService.createTenant(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<TenantResponse> getTenants(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) TenantStatus status,
        @RequestParam(required = false) TenantPlan plan,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "DESC") String sortDirection,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size
    ) {
        TenantSearchRequest request =
            new TenantSearchRequest(
                    search,
                    status,
                    plan,
                    sortBy,
                    sortDirection,
                    page,
                    size);
        return tenantService.getTenants(request);
    }

    @GetMapping("/{tenantId}")
    @ResponseStatus(HttpStatus.OK)
    public TenantResponse getTenant(@PathVariable UUID tenantId) {
        return tenantService.getTenant(tenantId);
    }

    @PutMapping("/{tenantId}")
    @ResponseStatus(HttpStatus.OK)
    public TenantResponse updateTenant(@PathVariable UUID tenantId, @RequestBody CreateTenantRequest request) {
        return tenantService.updateTenant(tenantId, request);
    }

    @PatchMapping("/{tenantId}/disable")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse disableTenant(@PathVariable UUID tenantId) {
        return tenantService.disableTenant(tenantId);
    }

    @PatchMapping("/{tenantId}/enable")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse enableTenant(@PathVariable UUID tenantId) {
        return tenantService.enableTenant(tenantId);
    }

}
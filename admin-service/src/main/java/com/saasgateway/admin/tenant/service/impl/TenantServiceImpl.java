package com.saasgateway.admin.tenant.service.impl;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.saasgateway.admin.common.exception.TenantNotFoundException;
import com.saasgateway.admin.tenant.dto.CreateTenantRequest;
import com.saasgateway.admin.tenant.dto.CreateTenantResponse;
import com.saasgateway.admin.tenant.dto.MessageResponse;
import com.saasgateway.admin.tenant.dto.TenantResponse;
import com.saasgateway.admin.tenant.dto.TenantSearchRequest;
import com.saasgateway.admin.tenant.entity.Tenant;
import com.saasgateway.admin.tenant.entity.TenantPlan;
import com.saasgateway.admin.tenant.entity.TenantStatus;
import com.saasgateway.admin.tenant.repository.TenantRepository;
import com.saasgateway.admin.tenant.service.TenantService;
import com.saasgateway.admin.tenant.specification.TenantSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;

    private static final Set<String> ALLOWED_SORT_FIELDS =
        Set.of(
                "name",
                "createdAt",
                "updatedAt",
                "status",
                "plan"
        );

    @Override
    public CreateTenantResponse createTenant(CreateTenantRequest request) {
        Tenant tenant = Tenant.builder()
                .id(UUID.randomUUID())
                .name(request.name())
                .plan(request.plan())
                .status(TenantStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        tenant = tenantRepository.save(tenant);

        return new CreateTenantResponse(
            tenant.getId(),
            tenant.getName(),
            tenant.getStatus(),
            tenant.getPlan()

        );
    }

    @Override
    public Page<TenantResponse> getTenants(TenantSearchRequest request) {
        String search = request.search();
        TenantStatus status = request.status();
        TenantPlan plan = request.plan();
        Specification<Tenant> spec = (root, query, cb) -> cb.conjunction();
        if (StringUtils.hasText(search)) {
            spec = spec.and(TenantSpecification.hasName(search));
        }
        if (status != null) {
            spec = spec.and(TenantSpecification.hasStatus(status));
        }
        if (plan != null) {
            spec = spec.and(TenantSpecification.hasPlan(plan));
        }
        
        String sortBy = request.sortBy();

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            sortBy = "createdAt";
        }

        Sort.Direction direction;

        try {
            direction = Sort.Direction.fromString(request.sortDirection());
        } catch (Exception ex) {
            direction = Sort.Direction.DESC;
        }

        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(request.page(), request.size(), sort);
        Page<Tenant> tenants = tenantRepository.findAll(spec, pageable);;
        return tenants.map(
            tenant -> new TenantResponse(
                tenant.getId(),
                tenant.getName(),
                tenant.getStatus(),
                tenant.getPlan()
            )
        );
    }

    @Override
    public TenantResponse getTenant(UUID tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new TenantNotFoundException(tenantId));
        return new TenantResponse(
            tenant.getId(),
            tenant.getName(),
            tenant.getStatus(),
            tenant.getPlan()
        );
    }

    @Override
    public TenantResponse updateTenant(UUID tenantId, CreateTenantRequest request) {
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(()-> new TenantNotFoundException(tenantId));
        if (StringUtils.hasText(request.name())) {
            tenant.setName(request.name());
        }
        if (Objects.nonNull(request.plan())){
            tenant.setPlan(request.plan());
        }
        tenant.setUpdatedAt(Instant.now());
        tenant = tenantRepository.save(tenant);
        return new TenantResponse(
            tenant.getId(),
            tenant.getName(),
            tenant.getStatus(),
            tenant.getPlan()
        );
    }

    @Override
    public MessageResponse disableTenant(UUID tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(()-> new TenantNotFoundException(tenantId));
        tenant.setStatus(TenantStatus.DISABLED);
        tenantRepository.save(tenant);
        return new MessageResponse("Tenant disabled successfully");
    }

    @Override
    public MessageResponse enableTenant(UUID tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(()-> new TenantNotFoundException(tenantId));
        tenant.setStatus(TenantStatus.ACTIVE);
        tenantRepository.save(tenant);
        return new MessageResponse("Tenant enabled successfully");
    }

}
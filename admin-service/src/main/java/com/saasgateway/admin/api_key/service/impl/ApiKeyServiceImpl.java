package com.saasgateway.admin.api_key.service.impl;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.saasgateway.admin.api_key.dto.ApiKeyResponse;
import com.saasgateway.admin.api_key.dto.ApiKeySearchRequest;
import com.saasgateway.admin.api_key.dto.CreateApiKeyRequest;
import com.saasgateway.admin.api_key.dto.CreateApiKeyResponse;
import com.saasgateway.admin.api_key.entity.ApiKey;
import com.saasgateway.admin.api_key.entity.ApiKeyStatus;
import com.saasgateway.admin.api_key.repository.ApiKeyRepository;
import com.saasgateway.admin.api_key.service.ApiKeyService;
import com.saasgateway.admin.api_key.specification.ApiKeySpecification;
import com.saasgateway.admin.api_key.util.ApiKeyGenerator;
import com.saasgateway.admin.api_key.util.ApiKeyHashUtil;
import com.saasgateway.admin.common.exception.ApiKeyNotFoundException;
import com.saasgateway.admin.common.exception.TenantNotFoundException;
import com.saasgateway.admin.tenant.dto.MessageResponse;
import com.saasgateway.admin.tenant.entity.Tenant;
import com.saasgateway.admin.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final TenantRepository tenantRepository;

    private static final Set<String>
        ALLOWED_SORT_FIELDS =
                Set.of(
                        "name",
                        "createdAt",
                        "lastUsedAt",
                        "status");
    
    @Override
    public MessageResponse activateApiKey(UUID apiKeyId) {
        ApiKey apiKey = apiKeyRepository.findById(apiKeyId).orElseThrow(() -> new ApiKeyNotFoundException(apiKeyId));
        apiKey.setStatus(ApiKeyStatus.ACTIVE);
        apiKeyRepository.save(apiKey);
        return new MessageResponse("Api Key activated successfully");
    }

    @Override
    public CreateApiKeyResponse createApiKey(UUID tenantId, CreateApiKeyRequest request) {
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(() -> new TenantNotFoundException(tenantId));

        String rawApiKey = ApiKeyGenerator.generate();

        String keyHash = ApiKeyHashUtil.hash(rawApiKey);

        ApiKey apiKey = ApiKey.builder()
                    .id(UUID.randomUUID())
                    .tenant(tenant)
                    .name(request.name())
                    .keyHash(keyHash)
                    .status(ApiKeyStatus.ACTIVE)
                    .createdAt(Instant.now())
                    .build();
                    
        apiKeyRepository.save(apiKey);
        return new CreateApiKeyResponse(
            apiKey.getId(),
            apiKey.getName(),
            rawApiKey
        );
    }

    @Override
    public ApiKeyResponse getApiKey(UUID apiKeyId) {
        ApiKey apiKey = apiKeyRepository.findById(apiKeyId).orElseThrow(() -> new ApiKeyNotFoundException(apiKeyId));
        return new ApiKeyResponse(
            apiKey.getId(),
            apiKey.getName(),
            apiKey.getStatus(),
            apiKey.getCreatedAt(),
            apiKey.getLastUsedAt()
        );
    }

    @Override
    public Page<ApiKeyResponse> getApiKeys(UUID tenantId, ApiKeySearchRequest request) {
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(() -> new TenantNotFoundException(tenantId));

        Specification<ApiKey> spec = ApiKeySpecification.belongsToTenant(tenantId);

        if (StringUtils.hasText(request.search())) {
            spec = spec.and(ApiKeySpecification.hasName(request.search()));
        }
        if (request.status() != null) {
            spec = spec.and(ApiKeySpecification.hasStatus(request.status()));
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

        Pageable pageable =PageRequest.of(request.page(), request.size(), Sort.by(direction, sortBy));
        Page<ApiKey> apiKeys = apiKeyRepository.findAll(spec, pageable);

        return apiKeys.map(
            apiKey -> new ApiKeyResponse(
                apiKey.getId(),
                apiKey.getName(),
                apiKey.getStatus(),
                apiKey.getCreatedAt(),
                apiKey.getLastUsedAt()
            )
        );
    }

    @Override
    public MessageResponse revokeApiKey(UUID apiKeyId) {
        ApiKey apiKey = apiKeyRepository.findById(apiKeyId).orElseThrow(() -> new ApiKeyNotFoundException(apiKeyId));
        apiKey.setStatus(ApiKeyStatus.REVOKED);
        apiKeyRepository.save(apiKey);
        return new MessageResponse("Api Key revoked successfully");
    }

}
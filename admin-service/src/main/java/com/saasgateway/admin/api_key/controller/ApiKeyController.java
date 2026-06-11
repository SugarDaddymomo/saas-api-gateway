package com.saasgateway.admin.api_key.controller;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.saasgateway.admin.api_key.dto.ApiKeyResponse;
import com.saasgateway.admin.api_key.dto.ApiKeySearchRequest;
import com.saasgateway.admin.api_key.dto.CreateApiKeyRequest;
import com.saasgateway.admin.api_key.dto.CreateApiKeyResponse;
import com.saasgateway.admin.api_key.entity.ApiKeyStatus;
import com.saasgateway.admin.api_key.service.ApiKeyService;
import com.saasgateway.admin.tenant.dto.MessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping("/tenants/{tenantId}/api-keys")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateApiKeyResponse createApiKey(@PathVariable UUID tenantId, @Valid @RequestBody CreateApiKeyRequest request) {
        return apiKeyService.createApiKey(tenantId, request);
    }

    @GetMapping("/tenants/{tenantId}/api-keys")
    @ResponseStatus(HttpStatus.OK)
    public Page<ApiKeyResponse> getTenantApiKeys(
        @PathVariable UUID tenantId,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) ApiKeyStatus status,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "DESC") String sortDirection,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size
    ) {
        ApiKeySearchRequest request =
                new ApiKeySearchRequest(
                    search,
                    status,
                    sortBy,
                    sortDirection,
                    page,
                    size);

        return apiKeyService.getApiKeys(tenantId, request);
    }

    @GetMapping("/api-keys/{apiKeyId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiKeyResponse getApiKey(@PathVariable UUID apiKeyId) {
        return apiKeyService.getApiKey(apiKeyId);
    }

    @PatchMapping("/api-keys/{apiKeyId}/revoke")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse revokeApiKey(@PathVariable UUID apiKeyId) {
        return apiKeyService.revokeApiKey(apiKeyId);
    }

    @PatchMapping("/api-keys/{apiKeyId}/activate")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse activateApiKey(@PathVariable UUID apiKeyId) {
        return apiKeyService.activateApiKey(apiKeyId);
    }
}
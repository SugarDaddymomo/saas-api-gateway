package com.saasgateway.admin.api_key.service;

import java.util.UUID;
import org.springframework.data.domain.Page;
import com.saasgateway.admin.api_key.dto.ApiKeyResponse;
import com.saasgateway.admin.api_key.dto.ApiKeySearchRequest;
import com.saasgateway.admin.api_key.dto.CreateApiKeyRequest;
import com.saasgateway.admin.api_key.dto.CreateApiKeyResponse;
import com.saasgateway.admin.tenant.dto.MessageResponse;

public interface ApiKeyService {

    CreateApiKeyResponse createApiKey(UUID tenantId, CreateApiKeyRequest request);

    Page<ApiKeyResponse> getApiKeys(UUID tenantId, ApiKeySearchRequest request);

    ApiKeyResponse getApiKey(UUID apiKeyId);

    MessageResponse revokeApiKey(UUID apiKeyId);

    MessageResponse activateApiKey(UUID apiKeyId);
}
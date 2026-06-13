package com.saasgateway.gateway_service.api_key.service.impl;

import java.time.Instant;
import org.springframework.stereotype.Service;
import com.saasgateway.gateway_service.api_key.entity.ApiKey;
import com.saasgateway.gateway_service.api_key.entity.ApiKeyStatus;
import com.saasgateway.gateway_service.api_key.repository.ApiKeyRepository;
import com.saasgateway.gateway_service.api_key.service.ApiKeyValidationService;
import com.saasgateway.gateway_service.api_key.util.ApiKeyHashUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiKeyValidationServiceImpl implements ApiKeyValidationService {

    private final ApiKeyRepository apiKeyRepository;

    @Override
    @Transactional
    public ApiKey validate(String rawApiKey) {

        String hash = ApiKeyHashUtil.hash(rawApiKey);
        ApiKey apiKey = apiKeyRepository.findByKeyHashWithTenant(hash)
                            .orElseThrow(() -> new RuntimeException("Invalid API Key"));

        if (apiKey.getStatus() != ApiKeyStatus.ACTIVE) {
            throw new RuntimeException("API Key revoked");
        }
        apiKeyRepository.updateLastUsedAt(apiKey.getId(), Instant.now());
        return apiKey;
    }
}
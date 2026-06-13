package com.saasgateway.gateway_service.api_key.service;

import com.saasgateway.gateway_service.api_key.entity.ApiKey;

public interface ApiKeyValidationService {
    ApiKey validate(String rawApiKey);
}
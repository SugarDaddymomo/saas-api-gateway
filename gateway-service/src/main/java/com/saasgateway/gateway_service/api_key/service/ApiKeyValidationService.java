package com.saasgateway.gateway_service.api_key.service;

import com.saasgateway.gateway_service.tenant.entity.Tenant;

public interface ApiKeyValidationService {

    Tenant validate(String rawApiKey);
}
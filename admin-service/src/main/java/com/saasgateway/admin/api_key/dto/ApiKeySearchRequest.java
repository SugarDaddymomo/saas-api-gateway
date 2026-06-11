package com.saasgateway.admin.api_key.dto;

import com.saasgateway.admin.api_key.entity.ApiKeyStatus;

public record ApiKeySearchRequest(
        String search,
        ApiKeyStatus status,
        String sortBy,
        String sortDirection,
        Integer page,
        Integer size
) {

}
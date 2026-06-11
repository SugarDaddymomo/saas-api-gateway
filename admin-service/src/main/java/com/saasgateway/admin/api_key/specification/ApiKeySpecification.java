package com.saasgateway.admin.api_key.specification;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.saasgateway.admin.api_key.entity.ApiKey;
import com.saasgateway.admin.api_key.entity.ApiKeyStatus;

public class ApiKeySpecification {

    public static Specification<ApiKey> hasName(String search) {
        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.get("name")),
                        "%" + search.toLowerCase() + "%");
    }

    public static Specification<ApiKey> hasStatus(ApiKeyStatus status) {
        return (root, query, cb) ->
                cb.equal(
                        root.get("status"),
                        status);
    }

    public static Specification<ApiKey> belongsToTenant(UUID tenantId) {
        return (root, query, cb) ->
                cb.equal(
                        root.get("tenant").get("id"),
                        tenantId);
    }
}
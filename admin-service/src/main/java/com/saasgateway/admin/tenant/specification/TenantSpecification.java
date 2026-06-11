package com.saasgateway.admin.tenant.specification;

import org.springframework.data.jpa.domain.Specification;

import com.saasgateway.admin.tenant.entity.Tenant;
import com.saasgateway.admin.tenant.entity.TenantPlan;
import com.saasgateway.admin.tenant.entity.TenantStatus;

public class TenantSpecification {

    public static Specification<Tenant> hasName(String search) {
        return (root, query, cb) ->
            cb.like(
                    cb.lower(root.get("name")),
                    "%" + search.toLowerCase() + "%");
    }


    public static Specification<Tenant> hasStatus(TenantStatus status) {
        return (root, query, cb) ->
            cb.equal(root.get("status"), status);
    }

    public static Specification<Tenant> hasPlan(TenantPlan plan) {
        return (root, query, cb) ->
            cb.equal(root.get("plan"), plan);
    }

}
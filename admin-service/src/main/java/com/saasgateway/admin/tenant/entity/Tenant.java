package com.saasgateway.admin.tenant.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tenant")
public class Tenant {

    @Id
    private UUID id;
    private String name;

    @Enumerated(EnumType.STRING)
    private TenantStatus status;
    @Enumerated(EnumType.STRING)
    private TenantPlan plan;

    @Column(name = "rate_limit")
    private Integer rateLimit;

    @Column(name = "rate_limit_window_seconds")
    private Integer rateLimitWindowSeconds;

    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;
}
CREATE TABLE route
(
    id UUID PRIMARY KEY,

    tenant_id UUID NOT NULL,

    name VARCHAR(255) NOT NULL,

    path_pattern VARCHAR(255) NOT NULL,

    target_url VARCHAR(500) NOT NULL,

    method VARCHAR(20) NOT NULL,

    status VARCHAR(50) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_route_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES tenant(id)
);

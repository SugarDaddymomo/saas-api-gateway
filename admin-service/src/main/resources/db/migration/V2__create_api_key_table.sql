CREATE TABLE api_key
(
    id UUID PRIMARY KEY,

    tenant_id UUID NOT NULL,

    name VARCHAR(255) NOT NULL,

    key_hash VARCHAR(255) NOT NULL,

    status VARCHAR(50) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    last_used_at TIMESTAMP,

    CONSTRAINT fk_api_key_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES tenant(id)
);

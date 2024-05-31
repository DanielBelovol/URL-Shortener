CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    lastname VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    locked BOOLEAN NOT NULL DEFAULT FALSE,
    role_id INTEGER REFERENCES roles(id)
);

CREATE TABLE url (
    id SERIAL PRIMARY KEY,
    short_url VARCHAR(255) NOT NULL UNIQUE,
    long_url TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID REFERENCES users(id),
    valid_to TIMESTAMP
);

CREATE TABLE url_views (
    id SERIAL PRIMARY KEY,
    url_id INTEGER REFERENCES url(id),
    ip_address VARCHAR(255),
    os_system VARCHAR(255),
    browser VARCHAR(255) NOT NULL,
    referer TEXT
);

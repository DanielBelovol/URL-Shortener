CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
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
    created_by INTEGER REFERENCES users(id),
    is_locked BOOLEAN NOT NULL DEFAULT FALSE,
    views INTEGER DEFAULT 0,
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
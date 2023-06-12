CREATE TABLE account
(
    id            SERIAL PRIMARY KEY,
    created_at    TIMESTAMP   NOT NULL,
    email         VARCHAR(63) NOT NULL UNIQUE,
    password_hash VARCHAR(63) NOT NULL,
    full_name     VARCHAR(63),
    type          VARCHAR(63) NOT NULL,
    is_verified   BOOLEAN     NOT NULL
);

CREATE TABLE refresh_token
(
    token      UUID PRIMARY KEY,
    account_id INT REFERENCES account (id) NOT NULL,
    created_at TIMESTAMP                   NOT NULL,
    expired_at TIMESTAMP                   NOT NULL
);

CREATE TABLE request_code
(
    id         SERIAL PRIMARY KEY,
    email      VARCHAR(63) NOT NULL,
    code       VARCHAR(63) NOT NULL,
    created_at TIMESTAMP   NOT NULL,
    is_used    BOOLEAN     NOT NULL
);

CREATE TABLE contractor_email
(
    id           SERIAL PRIMARY KEY,
    email        VARCHAR(63) NOT NULL UNIQUE,
    account_type VARCHAR(63) NOT NULL
);
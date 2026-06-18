-- liquibase formatted sql

-- changeset emerson:5
CREATE SEQUENCE IF NOT EXISTS seq_user
    INCREMENT 1
    START 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- changeset emerson:6
CREATE TABLE users (
    id BIGINT NOT NULL DEFAULT nextval('seq_user'),
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    role_id BIGINT,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uk_user_email UNIQUE (email),
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles (id)
);

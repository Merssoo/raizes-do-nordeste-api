-- liquibase formatted sql

-- changeset emerson:1
CREATE SEQUENCE IF NOT EXISTS seq_roles
    INCREMENT 1
    START 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- changeset emerson:2
CREATE TABLE roles (
    id BIGINT NOT NULL DEFAULT nextval('seq_roles'),
    nome VARCHAR(255) NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

-- changeset emerson:3
INSERT INTO roles (nome) VALUES ('ADMIN');
INSERT INTO roles (nome) VALUES ('GERENTE');
INSERT INTO roles (nome) VALUES ('COZINHA');
INSERT INTO roles (nome) VALUES ('ATENDENTE');
INSERT INTO roles (nome) VALUES ('CLIENTE');

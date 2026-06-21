-- liquibase formatted sql

-- changeset emerson:5
CREATE SEQUENCE IF NOT EXISTS seq_usuario
    INCREMENT 1
    START 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- changeset emerson:6
CREATE TABLE usuarios (
    id BIGINT NOT NULL DEFAULT nextval('seq_usuario'),
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    role_id BIGINT,
    CONSTRAINT pk_usuario PRIMARY KEY (id),
    CONSTRAINT uk_usuario_email UNIQUE (email),
    CONSTRAINT fk_usuario_role FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT ck_usuarios_nome CHECK (length(trim(nome)) > 0),
    CONSTRAINT ck_usuarios_email CHECK (email LIKE '%@%')
);

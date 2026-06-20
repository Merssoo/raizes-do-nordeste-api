--liquibase formatted sql

-- changeset emerson:1
CREATE SEQUENCE unidades_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE unidades (
    id BIGINT PRIMARY KEY DEFAULT nextval('unidades_id_seq'),
    nome VARCHAR(255) NOT NULL,
    cidade VARCHAR(255) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

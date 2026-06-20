--liquibase formatted sql

-- changeset emerson:1
CREATE SEQUENCE produtos_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE produtos (
    id BIGINT PRIMARY KEY DEFAULT nextval('produtos_id_seq'),
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(1000),
    preco DECIMAL(19, 2) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

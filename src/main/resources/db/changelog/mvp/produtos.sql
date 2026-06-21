--liquibase formatted sql

-- changeset emerson:1
CREATE SEQUENCE produtos_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE produtos (
    id BIGINT PRIMARY KEY DEFAULT nextval('produtos_id_seq'),
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(1000),
    preco DECIMAL(19, 2) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT ck_produtos_nome CHECK (length(trim(nome)) > 0),
    CONSTRAINT ck_produtos_preco CHECK (preco > 0),
    CONSTRAINT uk_produtos_nome_descricao UNIQUE (nome, descricao)
);

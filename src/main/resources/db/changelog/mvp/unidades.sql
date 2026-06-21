--liquibase formatted sql

-- changeset emerson:1
CREATE SEQUENCE unidades_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE unidades (
    id BIGINT PRIMARY KEY DEFAULT nextval('unidades_id_seq'),
    nome VARCHAR(255) NOT NULL,
    cidade VARCHAR(255) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT ck_unidades_nome CHECK (length(trim(nome)) > 0),
    CONSTRAINT ck_unidades_cidade CHECK (length(trim(cidade)) > 0),
    CONSTRAINT ck_unidades_estado CHECK (estado IN ('AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MT', 'MS', 'MG', 'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO')),
    CONSTRAINT uk_unidades_nome_cidade_estado UNIQUE (nome, cidade, estado)
);

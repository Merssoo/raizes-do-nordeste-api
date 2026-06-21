--liquibase formatted sql

-- changeset emerson:1
CREATE SEQUENCE estoques_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE estoques (
    id BIGINT PRIMARY KEY DEFAULT nextval('estoques_id_seq'),
    quantidade INTEGER NOT NULL,
    produto_id BIGINT NOT NULL REFERENCES produtos(id),
    unidade_id BIGINT NOT NULL REFERENCES unidades(id),
    CONSTRAINT ck_estoques_quantidade CHECK (quantidade >= 0),
    CONSTRAINT uk_estoques_produto_unidade UNIQUE (produto_id, unidade_id)
);

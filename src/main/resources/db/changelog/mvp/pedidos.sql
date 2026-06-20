--liquibase formatted sql

-- changeset emerson:1
CREATE SEQUENCE pedidos_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE pedidos (
    id BIGINT PRIMARY KEY DEFAULT nextval('pedidos_id_seq'),
    valor_total DECIMAL(19, 2) NOT NULL,
    canal_pedido VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario_id BIGINT REFERENCES usuarios(id),
    unidade_id BIGINT REFERENCES unidades(id)
);

CREATE SEQUENCE itens_pedido_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE itens_pedido (
    id BIGINT PRIMARY KEY DEFAULT nextval('itens_pedido_id_seq'),
    quantidade INTEGER NOT NULL,
    preco_unitario DECIMAL(19, 2) NOT NULL,
    subtotal DECIMAL(19, 2) NOT NULL,
    pedido_id BIGINT REFERENCES pedidos(id),
    produto_id BIGINT REFERENCES produtos(id)
);

CREATE SEQUENCE pagamentos_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE pagamentos (
    id BIGINT PRIMARY KEY DEFAULT nextval('pagamentos_id_seq'),
    valor DECIMAL(19, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    forma_pagamento VARCHAR(50) NOT NULL,
    data_pagamento TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    pedido_id BIGINT REFERENCES pedidos(id)
);

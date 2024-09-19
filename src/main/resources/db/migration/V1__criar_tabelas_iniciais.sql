-- Cria tabela Usuario
CREATE TABLE TB_DECKMKT_USUARIO (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    UNIQUE (email)
);

-- Cria tabela Categoria
CREATE TABLE TB_DECKMKT_CATEGORIA (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

-- Cria tabela Card
CREATE TABLE TB_DECKMKT_CARD (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    edicao VARCHAR(255) NOT NULL,
    preco DOUBLE NOT NULL,
    raridade VARCHAR(100) NOT NULL,
    categoria_id BIGINT NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES TB_DECKMKT_CATEGORIA(id)
);

-- Cria tabela Carrinho
CREATE TABLE TB_DECKMKT_CARRINHO (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES TB_DECKMKT_USUARIO(id)
);

-- Cria tabela de relacionamento entre Carrinho e Card
CREATE TABLE TB_DECKMKT_CARRINHO_CARD (
    carrinho_id BIGINT NOT NULL,
    card_id BIGINT NOT NULL,
    PRIMARY KEY (carrinho_id, card_id),
    FOREIGN KEY (carrinho_id) REFERENCES TB_DECKMKT_CARRINHO(id),
    FOREIGN KEY (card_id) REFERENCES TB_DECKMKT_CARD(id)
);

-- Cria tabela Pedido
CREATE TABLE TB_DECKMKT_PEDIDO (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    data_pedido TIMESTAMP NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES TB_DECKMKT_USUARIO(id)
);

-- Cria tabela de relacionamento entre Pedido e Card
CREATE TABLE TB_DECKMKT_PEDIDO_CARD (
    pedido_id BIGINT NOT NULL,
    card_id BIGINT NOT NULL,
    PRIMARY KEY (pedido_id, card_id),
    FOREIGN KEY (pedido_id) REFERENCES TB_DECKMKT_PEDIDO(id),
    FOREIGN KEY (card_id) REFERENCES TB_DECKMKT_CARD(id)
);

-- Cria tabela Pagamento
CREATE TABLE TB_DECKMKT_PAGAMENTO (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    metodo_pagamento VARCHAR(100) NOT NULL,
    status_pagamento VARCHAR(100) NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES TB_DECKMKT_PEDIDO(id)
);

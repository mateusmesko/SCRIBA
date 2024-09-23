-- V1__create_cartorio_tables.sql

CREATE TABLE situacao (
                           id VARCHAR(20) PRIMARY KEY,
                           nome VARCHAR(50) NOT NULL
);

INSERT INTO situacao (id, nome) VALUES ('SIT_ATIVO', 'Ativo'), ('SIT_BLOQUEADO', 'Bloqueado');

CREATE TABLE atribuicao (
    codigo SERIAL PRIMARY KEY,
     id VARCHAR(20),
     nome VARCHAR(50) NOT NULL,
     situacao BOOLEAN DEFAULT TRUE
);

CREATE TABLE cartorio (
  id SERIAL PRIMARY KEY,
  nome VARCHAR(150) NOT NULL,
  observacao VARCHAR(250),
  situacao_id VARCHAR(20),
  atribuicao_id VARCHAR(20)
);

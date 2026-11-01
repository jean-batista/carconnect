-- Criação do Banco de Dados
CREATE DATABASE IF NOT EXISTS carconnect;
USE carconnect;

-- 1. Tabela Base (Superclasse: Pessoa)
CREATE TABLE tabela_pessoa (
    id VARCHAR(36) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    sobrenome VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    cpf VARCHAR(14) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL
);

-- 2. Tabela Cliente (Subclasse de Pessoa)
CREATE TABLE tabela_cliente (
    id VARCHAR(36) PRIMARY KEY,
    CONSTRAINT fk_cliente_pessoa FOREIGN KEY (id) REFERENCES tabela_pessoa(id) ON DELETE CASCADE
);

-- 3. Tabela Vendedor (Subclasse de Pessoa)
CREATE TABLE tabela_vendedor (
    id VARCHAR(36) PRIMARY KEY,
    CONSTRAINT fk_vendedor_pessoa FOREIGN KEY (id) REFERENCES tabela_pessoa(id) ON DELETE CASCADE
);

-- 4. Tabela Gestor (Subclasse de Pessoa)
CREATE TABLE tabela_gestor (
    id VARCHAR(36) PRIMARY KEY,
    CONSTRAINT fk_gestor_pessoa FOREIGN KEY (id) REFERENCES tabela_pessoa(id) ON DELETE CASCADE
);

-- 5. Tabela Veiculo
CREATE TABLE tabela_veiculo (
    id VARCHAR(36) PRIMARY KEY,
    marca VARCHAR(100) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    ano INT NOT NULL,
    combustivel VARCHAR(50),
    condicao VARCHAR(50),
    preco DECIMAL(15, 2) NOT NULL,
    quantidade_estoque INT NOT NULL DEFAULT 0
);

-- 6. Relacionamento Gestor <-> Veiculo
-- Representa a List<Veiculo> dentro da classe Gestor
CREATE TABLE tabela_gestor_veiculo (
    gestor_id VARCHAR(36) NOT NULL,
    veiculo_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (gestor_id, veiculo_id),
    CONSTRAINT fk_gestor_id FOREIGN KEY (gestor_id) REFERENCES tabela_gestor(id) ON DELETE CASCADE,
    CONSTRAINT fk_veiculo_gestor_id FOREIGN KEY (veiculo_id) REFERENCES tabela_veiculo(id) ON DELETE CASCADE
);

-- 7. Tabela Agendamento
-- Vincula o Cliente e o Vendedor com suas respectivas chaves estrangeiras
CREATE TABLE tabela_agendamento (
    id VARCHAR(36) PRIMARY KEY,
    agendado_em DATETIME NOT NULL,
    agendado_para DATETIME NOT NULL,
    cliente_id VARCHAR(36) NOT NULL,
    vendedor_id VARCHAR(36) NOT NULL,
    CONSTRAINT fk_agendamento_cliente FOREIGN KEY (cliente_id) REFERENCES tabela_cliente(id) ON DELETE CASCADE,
    CONSTRAINT fk_agendamento_vendedor FOREIGN KEY (vendedor_id) REFERENCES tabela_vendedor(id) ON DELETE CASCADE
);

-- 8. Tabela Negociacao
-- Vincula o Cliente, o Vendedor e o Veiculo negociado
CREATE TABLE tabela_negociacao (
    id VARCHAR(36) PRIMARY KEY,
    data DATETIME NOT NULL,
    cliente_id VARCHAR(36) NOT NULL,
    vendedor_id VARCHAR(36) NOT NULL,
    veiculo_id VARCHAR(36) NOT NULL,
    CONSTRAINT fk_negociacao_cliente FOREIGN KEY (cliente_id) REFERENCES tabela_cliente(id) ON DELETE CASCADE,
    CONSTRAINT fk_negociacao_vendedor FOREIGN KEY (vendedor_id) REFERENCES tabela_vendedor(id) ON DELETE CASCADE,
    CONSTRAINT fk_negociacao_veiculo FOREIGN KEY (veiculo_id) REFERENCES tabela_veiculo(id) ON DELETE CASCADE
);
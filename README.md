# 🚗 CarConnect - Sistema de Gerenciamento de Concessionária

## 📋 Visão Geral do Projeto

O CarConnect é um sistema desenvolvido como parte do projeto A3 da faculdade. O objetivo principal da aplicação é otimizar e automatizar o gerenciamento das atividades cotidianas de uma concessionária de veículos, tornando os processos mais práticos, organizados e eficientes.

A aplicação oferece um ambiente integrado para o controle de estoque, cadastro de clientes, registro de vendas e acompanhamento de negociações, utilizando conceitos avançados de Programação Orientada a Objetos (POO) e persistência de dados relacional.

## 🚀 Principais Funcionalidades

- Gerenciamento de Clientes: Cadastro, atualização e consulta de informações de clientes.
- Controle de Veículos: Gestão completa do estoque de automóveis (marcas, modelos, anos, preços e status de disponibilidade).
- Registro de Vendas: Acompanhamento e registro de transações comerciais realizadas.
- Histórico de Negociações: Consulta detalhada das negociações passadas e em andamento.
- Interface Gráfica Intuitiva: Desenvolvida em Java para facilitar a interação do usuário com o sistema.

## 🛠️ Tecnologias Utilizadas

- Linguagem: Java (com foco em Programação Orientada a Objetos)
- Interface Gráfica (GUI): Java (Swing / JavaFX)
- Banco de Dados: MySQL

## 📦 Como Executar o Projeto

### Pré-requisitos:

Certifique-se de ter instalado em sua máquina:

- Java Development Kit (JDK 25 ou superior)
- MySQL Server
- Uma IDE de sua preferência (IntelliJ IDEA, Eclipse ou NetBeans)

### Passos para Instalação

1. Clone o repositório:

- ```git clone https://github.com/jean-batista/carconnect.git```

2. Configure o Banco de Dados:

- Crie um banco de dados MySQL com o nome carconnect.
- Execute o script SQL de criação das tabelas (disponível na pasta /database).

3. Configure a Conexão:

- Abra o projeto em sua IDE.
- Localize o arquivo de configuração de conexão com o banco de dados (ex: Conexao.java ou application.properties) e ajuste suas credenciais do MySQL (usuário e senha).

4. Execute a Aplicação:

- Localize a classe principal (Main ou App) e execute o projeto pela IDE.

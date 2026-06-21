# Raízes do Nordeste API

Esta API é o backend de um aplicativo móvel voltado para o cadastro de jogadores de futebol de categoria de base e para olheiros de clubes.

## Requisitos

- **Java:** Versão 21
- **Maven:** Versão 3.9+ (Wrapper incluso)
- **Banco de Dados:** PostgreSQL 15+
- **Gerenciamento de Migrations:** Liquibase

## Configuração de Variáveis de Ambiente

1. Crie um arquivo `.env` na raiz do projeto baseado no exemplo abaixo:

```bash
# .env.example
DB_URL=jdbc:postgresql://localhost:5432/raizes_db
DB_USERNAME=seu_usuario
DB_PASSWORD=sua_senha
JWT_SECRET=sua_chave_secreta_com_pelo_menos_32_caracteres
```

> **Nota:** Certifique-se de configurar essas variáveis no seu sistema ou arquivo de propriedades do Spring Boot.

## Instalação e Execução

### 1. Instalar Dependências
O projeto utiliza Maven Wrapper. Para baixar as dependências:

```bash
./mvnw clean install
```

### 2. Banco de Dados e Migrations
O projeto utiliza o Liquibase para gerenciar o banco de dados. Para aplicar as migrações e estruturar o banco, execute o seguinte comando:

```bash
./mvnw liquibase:update
```

Certifique-se de que o banco de dados esteja criado e que as credenciais no `.env` (ou `application.yaml`) estejam corretas antes de executar.

### 3. Iniciar a API
Para rodar a API em modo de desenvolvimento:

```bash
./mvnw spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

### 4. Documentação (Swagger/OpenAPI)
Após iniciar a aplicação, a documentação interativa pode ser acessada em:

`http://localhost:8080/swagger-ui/index.html`

### 5. Rodar os Testes
Para executar toda a suíte de testes unitários e de integração:

```bash
./mvnw test
```

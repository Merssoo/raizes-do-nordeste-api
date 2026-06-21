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

`http://www.localhost:8080/raizes/api/swagger-ui/index.html#/`

### 5. Rodar os Testes Automatizados
Para executar toda a suíte de testes unitários e de integração:

```bash
./mvnw test
```

## Validação Manual (Postman/Insomnia)

Para validar a API manualmente, utilize a coleção Postman entregue no arquivo `docs/postman_collection.json`.

### Como executar:
1. Importe o arquivo `Testes Raizes.postman_collection.json` no seu Postman ou Insomnia.
2. Configure o ambiente (Environment) apontando a variável `base_url` para `http://localhost:8080/raizes/api`.
3. **Ordem sugerida:**
   - Execute `Auth/Registro` para criar um usuário.
   - Execute `Auth/Login` e copie o `token` retornado.
   - Configure a variável de ambiente `token` com o valor copiado.
   - As demais requisições já estão configuradas para usar `{{token}}` no cabeçalho `Authorization` (tipo Bearer Token).
4. A coleção contém 10 cenários (6 positivos e 4 negativos) cobrindo autenticação, validação de dados, fluxo de pedidos e pagamentos.

> **Nota sobre Auditoria:** A funcionalidade de logs/auditoria de ações sensíveis não foi implementada neste MVP.

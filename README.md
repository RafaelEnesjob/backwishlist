# Wishlist API

Microserviço responsavel por gerenciar uma Wishlist(Lista de desejos do cliente). 

### ✅ Requisitos Funcionais

- Adicionar um produto na wishlist do cliente
- Remover um produto da wishlist do cliente
- Consultar todos os produtos da wishlist de um cliente
- Verificar se um produto específico está presente na wishlist de um cliente
- Limite de até 20 produtos por wishlist (validação incluída)

### ⚙️ Requisitos Técnicos

- Linguagem: Java 17
- Framework HTTP: Spring Boot 3
- Arquitetura: Clean Architecture (com separação em domain, usecases, infra, controllers, etc.)
- Testes automatizados com:
    - Testes de unidade (Junit 5)
    - Testes de comportamento (BDD com Cucumber)
- Banco de dados: MongoDB (NoSQL)
- Gerenciador de dependências: Maven
- Gerenciado por injeção de dependência com Spring Framework
- Design orientado a domínio (DDD)

## ⚙️ Stack Técnica Utilizada

Este projeto foi desenvolvido com foco em escalabilidade, modularidade e testabilidade, utilizando tecnologias modernas e seguindo boas práticas recomendadas pelo Luiza Labs.

### 📦 Dependências principais

- **Java 17** – Linguagem principal da aplicação.
- **Spring Boot 3.5.0** – Framework para criação do serviço HTTP e injeção de dependência.
- **MongoDB** – Banco de dados NoSQL para armazenar a Wishlist do cliente.
- **MapStruct 1.6.3** – Mapeamento de DTOs e Entities de forma performática.
- **Lombok** – Redução de boilerplate com geração automática de getters, setters e builders.

### 🧪 Testes

- **JUnit 5** – Testes unitários.
- **Cucumber 7.23.0** – Testes BDD com integração Spring.
- **Jacoco** – Verificação de cobertura de testes com metas de 100% em linha, branch e instrução.
- **Maven Surefire Plugin** – Execução dos testes durante o build.

### 📖 Documentação

- **Springdoc OpenAPI** – Geração automática da documentação da API no padrão Swagger (disponível em `/swagger-ui.html`).

### 🏗️ Configuração especial de build

- Perfis de cobertura de teste definidos via `jacoco-maven-plugin`, com limites mínimos de **100%**.
- Exclusão de classes como configuração, mapeamentos, respostas, exceptions e arquivos json dos relatórios de cobertura.

# Documentação

Para facilitar a execução do projeto, foi adicionado um arquivo docker-compose.yml, que cria dois contêineres:

MongoDB: banco de dados utilizado pela aplicação

Mongo Express: interface web para visualização dos dados

Após iniciar os contêineres, a interface do Mongo Express pode ser acessada em http://localhost:8081, utilizando:

**Usuário: admin**

**Senha: pass**

###  Iniciando o projeto

```bash
# Clone este repositório
git clone https://github.com/leonardodantas/wishlist.git

# Tenha o docker compose instalando, acesse a pasta raiz do projeto e execute o seguinte comando
docker-compose up --build

# O comando acima ira criar as seguintes instâncias
- Mongo
- Mongo Express
 
# Inicie a aplicação com uma IDE

#Acesse o seguinte endereço no navegador
http://localhost:8080/swagger-ui/index.html
```

---

### 🧩 Endpoint: Adicionar produto à wishlist

#### Método
`POST`

#### URL
`/api/v1/wishlist/{customerId}/products`

#### 📄 Descrição

Adiciona um produto à wishlist de um cliente. Caso o cliente ainda não possua uma wishlist, ela será criada automaticamente com o produto inserido.

---

#### 🔗 Parâmetros de Path

| Parâmetro     | Tipo     | Obrigatório | Descrição        |
|---------------|----------|-------------|------------------|
| `customerId`  | `string` | Sim         | ID do cliente    |

#### 📦 Parâmetros de Body

| Campo    | Tipo     | Obrigatório | Descrição             |
|----------|----------|-------------|------------------------|
| `id`     | `string` | Sim         | ID do produto          |
| `name`   | `string` | Sim         | Nome do produto        |

---

#### 📦 Corpo da Requisição (`ProductRequest`)

```json
{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "name": "Tênis Nike"
}
```

#### Regras de Negócio

🧠 Regras de Negócio
- A wishlist deve conter no máximo 20 produtos.
- Não é permitido adicionar um produto que já exista na wishlist.
- Se o cliente não tiver uma wishlist cadastrada, uma nova será criada automaticamente.

#### Resposta

- **Status:** `201 Created`
- **Body:** Nenhum conteúdo retornado.

#### Exemplo de curl

```
curl -X POST \
  http://localhost:8080/api/v1/wishlist/cliente123/products \
  -H "Content-Type: application/json" \
  -d '{
    "id": "pdt-001",
    "name": "Echo Dot 5ª geração"
  }'
  
```

#### Erros Possíveis

| Código HTTP | Mensagem de Erro                                         |
|-------------|----------------------------------------------------------|
| `404`       | `"Wishlist não encontrada (em caso de inconsistência)."` |
| `409`       | `"Produto já existente na wishlist ou limite excedido."` |
| `400`       | `"Erro de validação no corpo da requisição"` | 

---

### 🗑️ Endpoint: Remover produto da wishlist

#### Método
`DELETE`

#### URL
`/api/v1/wishlist/{customerId}/products/{productId}`

#### 📄 Descrição

Remove um produto da wishlist de um cliente.  
Caso a wishlist não exista ou o produto não esteja presente nela, será retornado erro de negócio adequado.

---

#### 🔗 Parâmetros de Path

| Parâmetro     | Tipo     | Obrigatório | Descrição                 |
|---------------|----------|-------------|---------------------------|
| `customerId`  | `string` | Sim         | ID do cliente             |
| `productId`   | `string` | Sim         | ID do produto a remover   |

---

#### 🧠 Regras de Negócio

- Se o cliente não possuir uma wishlist cadastrada, o sistema retorna erro `WishlistNotFoundException`.
- Se o produto informado não estiver presente na wishlist, o sistema retorna erro `ProductNotFoundException`.
- Caso o produto exista, ele será removido da wishlist e a lista será atualizada no repositório.

---

#### ✅ Respostas

| Código HTTP | Descrição                                                   |
|-------------|--------------------------------------------------------------|
| `200`       | Produto removido com sucesso                                 |
| `404`       | Wishlist não encontrada ou produto não está na lista         |

---

#### 🧪 Exemplo de requisição `curl`

```bash
curl -X DELETE \
  http://localhost:8080/api/v1/wishlist/cliente123/products/pdt-001 \
  -H "accept: */*"
```
---


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

### 📥 Endpoint: Buscar todos os produtos da wishlist do cliente

#### Método
`GET`

#### URL
`/api/v1/wishlist/{customerId}/products`

---

#### 📄 Descrição

Busca e retorna **todos os produtos** associados à wishlist de um cliente específico.

- Se a wishlist existir e tiver produtos: retorna a lista com todos os produtos.
- Se a wishlist existir mas estiver vazia: retorna uma lista vazia.
- Se a wishlist **não existir** para o cliente informado: retorna erro `404`.

---

#### 🔗 Parâmetros de Path

| Parâmetro     | Tipo     | Obrigatório | Descrição              |
|---------------|----------|-------------|------------------------|
| `customerId`  | `string` | Sim         | ID do cliente da wishlist |

---

#### 🔄 Exemplo de Resposta (200 OK)

```json
{
  "id": "wishlist-1",
  "customerId": "cliente123",
  "products": [
    {
      "id": "pdt-001",
      "name": "Tenis Nike"
    },
    {
      "id": "pdt-002",
      "name": "Tenis Adidas"
    }
  ]
}
```
#### 🧪 Exemplo com lista vazia

```json
{
  "id": "wishlist-1",
  "customerId": "cliente123",
  "products": []
}
```
🧠 Regras de Negócio
A wishlist deve estar vinculada ao customerId informado.

O retorno pode conter de 0 até 20 produtos.

Se não houver wishlist cadastrada para o cliente, o sistema retorna erro WishlistNotFoundException.

---

#### Erros Possíveis

| Código HTTP | Mensagem de Erro                                         |
|-------------|----------------------------------------------------------|
| `404`       | `"Wishlist não encontrada (em caso de inconsistência)."` |


#### 🧪 Exemplo de requisição `curl`

```bash
curl -X GET \
  http://localhost:8080/api/v1/wishlist/cliente123/products \
  -H "accept: application/json"
```
---

### 🔍 Endpoint: Buscar um produto específico na wishlist

#### Método
`GET`

#### URL
`/api/v1/wishlist/{customerId}/products/{productId}`

---

#### 📄 Descrição

Verifica se **um produto específico** está presente na wishlist de um cliente e o retorna.

- Se a wishlist existir e o produto estiver nela, retorna o produto.
- Se a wishlist não existir ou o produto não estiver presente, retorna erro `404`.

---

#### 🔗 Parâmetros de Path

| Parâmetro     | Tipo     | Obrigatório | Descrição                           |
|---------------|----------|-------------|-------------------------------------|
| `customerId`  | `string` | Sim         | ID do cliente da wishlist           |
| `productId`   | `string` | Sim         | ID do produto a ser consultado      |

---

#### ✅ Exemplo de Resposta (200 OK)

```json
{
  "id": "wishlist-1",
  "customerId": "cliente123",
  "products": [
    {
      "id": "pdt-001",
      "name": "Echo Dot 5ª geração"
    }
  ]
}
```
🧠 Regras de Negócio
A wishlist deve estar vinculada ao customerId informado.

O produto consultado deve existir dentro da lista de produtos da wishlist.

Caso não exista wishlist ou o produto não esteja presente, o sistema lança a exceção adequada:

WishlistNotFoundException

ProductNotFoundException

---
#### Erros Possíveis

| Código HTTP | Mensagem de Erro                                       |
|-------------|--------------------------------------------------------|
| `404`       | `"Wishlist não encontrada ou produto inexistente"` |

#### 🧪 Exemplo de requisição `curl`

```bash
curl -X GET \
  http://localhost:8080/api/v1/wishlist/cliente123/products/pdt-001 \
  -H "accept: application/json"
```

---

🧱 Arquitetura da Aplicação
A aplicação foi construída seguindo os princípios da Clean Architecture, proposta por Robert C. Martin, com foco em:

* Separação de responsabilidades

* Alta coesão e baixo acoplamento

* Independência de frameworks

* Independência de banco de dados

* Testabilidade
---
🔹 Padrões aplicados
DDD (Domain-Driven Design) para modelagem rica de negócio

Builder Pattern com Lombok (@Builder)

Ports & Adapters (Hexagonal) para abstração de dependências externas

Mapper Layer com MapStruct para conversão de entidade ↔ DTO

---

🧱 Arquitetura e Organização do Projeto
Este projeto segue os princípios da Clean Architecture, promovendo uma separação clara entre as regras de negócio (domínio), casos de uso, interfaces de entrada/saída e detalhes de infraestrutura.


com.backwishlist
├── api                    # Camada de entrada da aplicação (Controller REST)
│   ├── controllers        # Exposição de endpoints via Spring MVC
│   ├── dtos               # Objetos de transferência (request/response)
│   └── config             # Configurações globais (ex: OpenAPI)
│
├── app                    # Casos de uso e portas de entrada/saída
│   ├── repositories       # Interface (porta de saída) para persistência
│   └── usecases           # Regras de negócio (Application Layer)
│       └── impl           # Implementações dos casos de uso
│
├── domain                 # Camada de domínio (Enterprise Business Rules)
│   ├── exceptions         # Exceções específicas do domínio
│   ├── Product.java       # Entidade de domínio: Produto
│   └── Wishlist.java      # Entidade de domínio: Wishlist
│
├── infrastructure         # Implementações de detalhes técnicos (Mongo, Mappers)
│   ├── database           # Implementação de persistência (MongoDB)
│   │   ├── documents      # Representações do modelo no banco
│   │   └── repositories   # Interfaces de dados do MongoDB
│   └── mappers            # Conversores entre entidade e documento com MapStruct
│
└── ApiApplication.java    # Classe principal (entrypoint Spring Boot)

### 🗂️ Estrutura de Pastas do Projeto

```text
com.backwishlist
├── api                    # Camada de entrada da aplicação (Controller REST)
│   ├── controllers        # Exposição de endpoints via Spring MVC
│   ├── dtos               # Objetos de transferência (request/response)
│   └── config             # Configurações globais (ex: OpenAPI)
│
├── app                    # Casos de uso e portas de entrada/saída
│   ├── repositories       # Interface (porta de saída) para persistência
│   └── usecases           # Regras de negócio (Application Layer)
│       └── impl           # Implementações dos casos de uso
│
├── domain                 # Camada de domínio (Enterprise Business Rules)
│   ├── exceptions         # Exceções específicas do domínio
│   ├── Product.java       # Entidade de domínio: Produto
│   └── Wishlist.java      # Entidade de domínio: Wishlist
│
├── infrastructure         # Implementações de detalhes técnicos (Mongo, Mappers)
│   ├── database           # Implementação de persistência (MongoDB)
│   │   ├── documents      # Representações do modelo no banco
│   │   └── repositories   # Interfaces de dados do MongoDB
│   └── mappers            # Conversores entre entidade e documento com MapStruct
│
└── ApiApplication.java    # Classe principal (entrypoint Spring Boot)
```
---

✅ Boas Práticas Aplicadas
* Utilização de princípios SOLID

* Organização por camadas independentes

* Classes de teste desacopladas do framework

* Factories e Fixtures para construção de cenários

* Validações e exceções personalizadas

* Uso de record para simplificar DTOs

* Testes com 100% de cobertura (line/branch/instruction) via Jacoco

🔍 Orientação a Objetos
A modelagem segue forte orientação a objetos:

* O domínio é modelado com comportamento e estado encapsulados (Wishlist, Product)

* Métodos como addProduct, containsProduct, canAddMoreProducts encapsulam regras de negócio

* Exceções são lançadas diretamente do domínio — o comportamento está no lugar certo

🧪 Estratégia de Testes
🧱 Testes Unitários
Realizados com JUnit 5, cobrindo:

* Casos positivos e negativos de cada use case

* Comportamentos do domínio (Wishlist, Product)

Regras como:

* Limite de produtos

* Produto duplicado

* Remoção de produtos inexistentes

🔁 Testes Integrados
Utilizando MockMvc, cobrem:

* Controllers expostos

* Serialização JSON

* Integração com camada de uso

🧬 Testes BDD (Cucumber)
Especificações comportamentais para todos os casos de uso:

**OBS:** Para rodar esse teste, digite no terminal "mvn clean test"

Este projeto adota a abordagem BDD para testes de comportamento, utilizando Cucumber 7+ com integração ao Spring Boot, e executado via junit-platform.properties, sem necessidade de classe runner.

⚙️ Configuração via junit-platform.properties
Você optou por não usar uma classe @CucumberTest, então o Cucumber é executado com o seguinte arquivo:

* Features organizadas por responsabilidade

* Steps separados por contexto (AddProduct, RemoveProduct, GetWishlist)

* Execução com perfil de test, banco isolado e MongoTemplate limpo a cada teste

---
### Exemplo de Fluxo de Uso (Clean Architecture) ###

Controller → UseCase → Domain → Repository (interface) → Infra (MongoRepository)

Exemplo: Adicionar produto à wishlist

* 1 - O controller recebe a requisição e converte para Product.

* 2 - Chama AddProductToWishlistUseCase.

* 3 - A Wishlist é buscada e verificada (existência, duplicidade, limite).

* 4 - Produto é adicionado e lista atualizada.

* 5 - Repository salva o novo estado.
---

🧩 Pontos de Evolução
Integração com mensageria (ex: Kafka) para eventos assíncronos

Autenticação com Spring Security e JWT

Deploy automatizado com Docker + CI/CD (ex: GitHub Actions)

Monitoração com Grafana
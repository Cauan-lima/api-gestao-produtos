API de Gestão de Produtos

Uma API REST completa desenvolvida com **Spring Boot** para gerenciamento de produtos, controle de estoque, categorias, marcas e fornecedores. 

A aplicação conta com arquitetura em camadas, suporte a **HATEOAS** (navegação por hipermídia), paginação dinâmica com `Pageable` e documentação interativa com **Swagger / OpenAPI**.

---

Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Data JPA** (persistência e consultas)
- **Spring HATEOAS** (links hipermídia nos recursos)
- **SpringDoc OpenAPI / Swagger UI** (documentação interativa da API)
- **H2 Database** (banco de dados em memória para testes e desenvolvimento rápido)
- **Maven** (gerenciamento de dependências e build)

---
Modelo de Dados e Relacionamentos

A entidade **Product** funciona como o núcleo do sistema e se conecta com as outras entidades da seguinte forma:

```
+-----------+       1 : 1       +-----------+
|  Product  | ----------------- |   Stock   | (Estoque exclusivo por produto)
+-----------+                   +-----------+
      |
      | N : 1
      +-------------------------> [ Brand ] (Muitos produtos para uma Marca)
      |
      | N : 1
      +-------------------------> [ Category ] (Muitos produtos para uma Categoria)
      |
      | N : N
      +-------------------------> [ Supplier ] (Muitos produtos para muitos Fornecedores)
```

- **Brand (Marca):** Cada produto pertence a uma marca.
- **Category (Categoria):** Cada produto pertence a uma categoria.
- **Stock (Estoque):** Relação **1 para 1** (cada produto possui seu registro próprio de estoque).
- **Supplier (Fornecedor):** Relação **N para N** (um produto pode ser fornecido por diversos parceiros).

---

Como usar a API (Ordem de Cadastro)

Como o sistema trabalha com relacionamentos no banco, **o Produto depende de dados que já precisam existir previamente**. 

Para cadastrar um produto sem erros de chave estrangeira, siga esta ordem:

### Passo 1: Cadastrar a Marca
> `POST /brand`

```json
{
  "nome": "Dell",
  "pais": "Estados Unidos"
}
```
*Guarde o `id` retornado (ex: `1`).*

---

### Passo 2: Cadastrar a Categoria
> `POST /category`

```json
{
  "nome": "Informática",
  "descricao": "Computadores, notebooks e acessórios"
}
```
*Guarde o `id` retornado (ex: `1`).*

---

### Passo 3: Cadastrar o Estoque
> `POST /stock`

```json
{
  "quantidade": 30,
  "estoqueMinimo": 5
}
```
*Guarde o `id` retornado (ex: `1`).*

---

### Passo 4: Cadastrar o(s) Fornecedor(es)
> `POST /supplier`

```json
{
  "nome": "Distribuidora Tech Brasil",
  "email": "contato@techbrasil.com.br"
}
```
*Guarde o `id` ou os `ids` gerados em uma lista (ex: `[1]`).*

---

### Passo 5: Cadastrar o Produto
> `POST /product`

Agora junte as informações do item e passe os IDs criados nos passos anteriores:

```json
{
  "nome": "Notebook Dell Inspiron 15",
  "preco": 4299.90,
  "quantidade": 15,
  "brandId": 1,
  "categoryId": 1,
  "stockId": 1,
  "supplierIds": [1]
}
```

---

Recursos Especiais da API

### 1. Paginação e Ordenação Dinâmica (`Pageable`)
Todos os endpoints de listagem (`GET`) possuem paginação ativada por padrão:

- `?page=0`: número da página (inicia em 0).
- `?size=10`: quantidade de registros por página.
- `?sort=nome,asc`: ordenação por qualquer campo da entidade (`asc` ou `desc`).

**Exemplo:**
```http
GET /product?page=0&size=5&sort=preco,desc
```

### 2. Navegação HATEOAS
As respostas da API seguem o modelo de hipermídia. Cada item retornado traz o bloco `_links` apontando para suas ações e entidades vinculadas:

```json
{
  "_embedded": {
    "productList": [
      {
        "id": 1,
        "nome": "Notebook Dell Inspiron 15",
        "preco": 4299.9,
        "quantidade": 15,
        "_links": {
          "self": { "href": "http://localhost:8080/product/1" },
          "produtos": { "href": "http://localhost:8080/product" },
          "brand": { "href": "http://localhost:8080/brand/1" },
          "category": { "href": "http://localhost:8080/category/1" },
          "stock": { "href": "http://localhost:8080/stock/1" }
        }
      }
    ]
  },
  "_links": {
    "first": { "href": "http://localhost:8080/product?page=0&size=5" },
    "self": { "href": "http://localhost:8080/product?page=0&size=5" },
    "last": { "href": "http://localhost:8080/product?page=0&size=5" }
  },
  "page": {
    "size": 5,
    "totalElements": 1,
    "totalPages": 1,
    "number": 0
  }
}
```

---

Resumo dos Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/product` | Cria um novo produto vinculando IDs |
| `GET` | `/product` | Lista produtos paginados com links HATEOAS |
| `GET` | `/product/{id}` | Busca produto por ID |
| `PUT` | `/product/{id}` | Atualiza dados do produto |
| `DELETE` | `/product/{id}` | Remove um produto |
| `POST` | `/brand` | Cadastra nova marca |
| `GET` | `/brand` | Lista marcas paginadas |
| `GET` | `/brand/{id}` | Busca marca por ID |
| `PUT` | `/brand/{id}` | Atualiza marca |
| `DELETE` | `/brand/{id}` | Remove marca |
| `POST` | `/category` | Cadastra nova categoria |
| `GET` | `/category` | Lista categorias paginadas |
| `GET` | `/category/{id}` | Busca categoria por ID |
| `PUT` | `/category/{id}` | Atualiza categoria |
| `DELETE` | `/category/{id}` | Remove categoria |
| `POST` | `/stock` | Cadastra novo registro de estoque |
| `GET` | `/stock` | Lista registros de estoque |
| `GET` | `/stock/{id}` | Busca estoque por ID |
| `PUT` | `/stock/{id}` | Atualiza estoque |
| `DELETE` | `/stock/{id}` | Remove estoque |
| `POST` | `/supplier` | Cadastra novo fornecedor |
| `GET` | `/supplier` | Lista fornecedores paginados |
| `GET` | `/supplier/{id}` | Busca fornecedor por ID |
| `PUT` | `/supplier/{id}` | Atualiza fornecedor |
| `DELETE` | `/supplier/{id}` | Remove fornecedor |

---

Como Rodar o Projeto

### Pré-requisitos
- **Java JDK 17** instalado
- **Git**

### Executando localmente
1. Clone o repositório:
```bash
git clone <URL_DO_REPOSITORIO>
cd api-gestao-produtos
```

2. Execute o projeto usando o Maven Wrapper:

**Linux / Mac:**
```bash
./mvnw spring-boot:run
```

**Windows (PowerShell ou Prompt):**
```powershell
.\mvnw.cmd spring-boot:run
```

3. Acesse no navegador:
- **Swagger UI (Documentação):** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Console do Banco H2:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - **JDBC URL:** `jdbc:h2:mem:testdb`
  - **Usuário:** `sa`
  - **Senha:** *(deixar em branco)*



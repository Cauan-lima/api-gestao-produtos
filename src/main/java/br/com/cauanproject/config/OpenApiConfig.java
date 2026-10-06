package br.com.cauanproject.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        String descricao = """
                ### Bem-vindo à API de Gestão de Produtos! 👋
                
                Aqui você encontra todos os endpoints para cadastrar e gerenciar produtos, marcas, categorias, estoques e fornecedores.
                
                ---
                
                ### ⚠️ Como usar a API (Ordem recomendada de cadastro)
                
                Nosso sistema funciona em camadas. Isso significa que **um Produto precisa de dados que já foram cadastrados antes**. Se você tentar criar um produto primeiro, ele vai falhar porque espera os IDs das outras entidades.
                
                Siga esse passo a passo para cadastrar sem erro:
                
                1. **Cadastre a Marca** (`POST /brand`)
                   - Exemplo: `{"nome": "Dell", "pais": "Estados Unidos"}`
                   - Guarde o `id` retornado.
                
                2. **Cadastre a Categoria** (`POST /category`)
                   - Exemplo: `{"nome": "Informática", "descricao": "Computadores e periféricos"}`
                   - Guarde o `id` retornado.
                
                3. **Cadastre o Estoque** (`POST /stock`)
                   - Exemplo: `{"quantidade": 20, "estoqueMinimo": 5}`
                   - Relação **1 para 1** (cada produto tem seu próprio controle de estoque).
                   - Guarde o `id` retornado.
                
                4. **Cadastre um ou mais Fornecedores** (`POST /supplier`)
                   - Exemplo: `{"nome": "Distribuidora Tech", "email": "contato@tech.com"}`
                   - Relação **Muitos para Muitos** (um produto pode ter vários fornecedores).
                   - Guarde os `ids` gerados em uma lista.
                
                5. **Por fim, cadastre o Produto** (`POST /product`)
                   - Junte os IDs que você gerou nos passos anteriores e monte o payload:
                   
                   ```json
                   {
                     "nome": "Notebook Dell Inspiron",
                     "preco": 4200.00,
                     "quantidade": 15,
                     "brandId": 1,
                     "categoryId": 1,
                     "stockId": 1,
                     "supplierIds": [1]
                   }
                   ```
                
                ---
                
                ### 💡 Recursos extras
                - **Paginação:** Todos os endpoints de listagem aceitam os parâmetros `?page=0&size=10&sort=nome,asc`.
                - **HATEOAS:** As respostas trazem links diretos (`_links`) para o próprio recurso, rotas de listagem e entidades relacionadas, facilitando a navegação na API.
                """;

        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestão de Produtos")
                        .version("1.0")
                        .description(descricao)
                        .contact(new Contact()
                                .name("Cauan Lima")
                                .email("cauanmatheuscalima@gmail.com")));
    }
}
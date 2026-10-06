package br.com.cauanproject;

import br.com.cauanproject.entity.Brand;
import br.com.cauanproject.entity.Category;
import br.com.cauanproject.entity.Product;
import br.com.cauanproject.entity.Stock;
import br.com.cauanproject.repository.BrandRepository;
import br.com.cauanproject.repository.CategoryRepository;
import br.com.cauanproject.repository.ProductRepository;
import br.com.cauanproject.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class HateoasPaginationIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldReturnPaginatedHateoasResponseForBrands() throws Exception {
        Brand brand1 = new Brand();
        brand1.setNome("Sony");
        brand1.setPais("Japão");
        brandRepository.save(brand1);

        Brand brand2 = new Brand();
        brand2.setNome("Samsung");
        brand2.setPais("Coreia do Sul");
        brandRepository.save(brand2);

        mockMvc.perform(get("/brand?page=0&size=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.brandList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.brandList[0]._links.self.href", notNullValue()))
                .andExpect(jsonPath("$._embedded.brandList[0]._links.marcas.href", notNullValue()))
                .andExpect(jsonPath("$._links.first.href", notNullValue()))
                .andExpect(jsonPath("$._links.self.href", notNullValue()))
                .andExpect(jsonPath("$._links.next.href", notNullValue()))
                .andExpect(jsonPath("$._links.last.href", notNullValue()))
                .andExpect(jsonPath("$.page.size", is(1)))
                .andExpect(jsonPath("$.page.totalElements", is(2)))
                .andExpect(jsonPath("$.page.totalPages", is(2)))
                .andExpect(jsonPath("$.page.number", is(0)));
    }

    @Test
    void shouldReturnHateoasLinksForSingleBrand() throws Exception {
        Brand brand = new Brand();
        brand.setNome("Apple");
        brand.setPais("EUA");
        brand = brandRepository.save(brand);

        mockMvc.perform(get("/brand/" + brand.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(brand.getId().intValue())))
                .andExpect(jsonPath("$.nome", is("Apple")))
                .andExpect(jsonPath("$._links.self.href", containsString("/brand/" + brand.getId())))
                .andExpect(jsonPath("$._links.marcas.href", containsString("/brand")));
    }

    @Test
    void shouldReturnHateoasLinksWhenCreatingBrand() throws Exception {
        String requestJson = """
                {
                    "nome": "Logitech",
                    "pais": "Suíça"
                }
                """;

        mockMvc.perform(post("/brand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is("Logitech")))
                .andExpect(jsonPath("$._links.self.href", notNullValue()))
                .andExpect(jsonPath("$._links.marcas.href", containsString("/brand")));
    }

    @Test
    void shouldReturnPaginatedHateoasResponseForProducts() throws Exception {
        Brand brand = new Brand();
        brand.setNome("Dell");
        brand.setPais("EUA");
        brand = brandRepository.save(brand);

        Category category = new Category();
        category.setNome("Informática");
        category.setDescricao("Equipamentos de informática");
        category = categoryRepository.save(category);

        Stock stock = new Stock();
        stock.setQuantidade(50);
        stock.setEstoqueMinimo(5);
        stock = stockRepository.save(stock);

        Product product = new Product();
        product.setNome("Notebook Dell XPS");
        product.setPreco(8500.0);
        product.setQuantidade(10);
        product.setBrand(brand);
        product.setCategory(category);
        product.setStock(stock);
        product = productRepository.save(product);

        mockMvc.perform(get("/product?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.productList[0].nome", is("Notebook Dell XPS")))
                .andExpect(jsonPath("$._embedded.productList[0]._links.self.href", containsString("/product/" + product.getId())))
                .andExpect(jsonPath("$._embedded.productList[0]._links.produtos.href", containsString("/product")))
                .andExpect(jsonPath("$._embedded.productList[0]._links.brand.href", containsString("/brand/" + brand.getId())))
                .andExpect(jsonPath("$._embedded.productList[0]._links.category.href", containsString("/category/" + category.getId())))
                .andExpect(jsonPath("$._embedded.productList[0]._links.stock.href", containsString("/stock/" + stock.getId())))
                .andExpect(jsonPath("$.page.totalElements", is(1)))
                .andExpect(jsonPath("$.page.number", is(0)));
    }
}

package br.com.cauanproject.controller;

import br.com.cauanproject.assembler.ProductModelAssembler;
import br.com.cauanproject.dto.ProductRequest;
import br.com.cauanproject.entity.Product;
import br.com.cauanproject.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@Tag(
        name = "Produtos",
        description = "Operações de gerenciamento de produtos"
)
public class ProductController {

    private final ProductService service;
    private final ProductModelAssembler assembler;

    public ProductController(ProductService service, ProductModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    // POST
    @Operation(
            summary = "Criar produto",
            description = "Cadastra um novo produto utilizando os IDs das entidades relacionadas"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Produto criado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            )
    })
    @PostMapping
    public ResponseEntity<EntityModel<Product>> criar(
            @RequestBody ProductRequest request) {

        Product criado = service.salvar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(criado));
    }

    // GET ALL COM PAGINAÇÃO E HATEOAS
    @Operation(
            summary = "Listar produtos",
            description = "Retorna os produtos cadastrados com paginação e links HATEOAS"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Produtos encontrados"
    )
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Product>>> listar(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @Parameter(hidden = true) PagedResourcesAssembler<Product> pagedResourcesAssembler) {

        Page<Product> produtos = service.listarTodos(pageable);

        return ResponseEntity.ok(pagedResourcesAssembler.toModel(produtos, assembler));
    }

    // GET BY ID
    @Operation(
            summary = "Buscar produto por ID",
            description = "Busca um produto específico através do seu ID com links HATEOAS"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto encontrado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Product>> buscarPorId(
            @PathVariable Long id) {

        return service.buscarPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT
    @Operation(
            summary = "Atualizar produto",
            description = "Atualiza os dados de um produto existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto atualizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Product>> atualizar(
            @PathVariable Long id,
            @RequestBody Product product) {

        return service.atualizar(id, product)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @Operation(
            summary = "Excluir produto",
            description = "Remove um produto do sistema"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Produto excluído com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        if (!service.deletar(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
package br.com.cauanproject.controller;

import br.com.cauanproject.dto.ProductRequest;
import br.com.cauanproject.entity.Product;
import br.com.cauanproject.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@Tag(
        name = "Produtos",
        description = "Operações de gerenciamento de produtos"
)
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
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
    public ResponseEntity<Product> criar(
            @RequestBody ProductRequest request) {

        Product criado = service.salvar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(criado);
    }

    // GET ALL
    @Operation(
            summary = "Listar produtos",
            description = "Retorna todos os produtos cadastrados"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Produtos encontrados"
    )
    @GetMapping
    public ResponseEntity<List<Product>> listar() {

        return ResponseEntity.ok(service.listarTodos());
    }

    // GET BY ID
    @Operation(
            summary = "Buscar produto por ID",
            description = "Busca um produto específico através do seu ID"
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
    public ResponseEntity<Product> buscarPorId(
            @PathVariable Long id) {

        return service.buscarPorId(id)
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
    public ResponseEntity<Product> atualizar(
            @PathVariable Long id,
            @RequestBody Product product) {

        return service.atualizar(id, product)
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
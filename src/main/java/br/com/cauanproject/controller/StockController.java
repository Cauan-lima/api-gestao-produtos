package br.com.cauanproject.controller;

import br.com.cauanproject.assembler.StockModelAssembler;
import br.com.cauanproject.entity.Stock;
import br.com.cauanproject.service.StockService;
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
@RequestMapping("/stock")
@Tag(
        name = "Estoques",
        description = "Operações de gerenciamento de estoques"
)
public class StockController {

    private final StockService service;
    private final StockModelAssembler assembler;

    public StockController(StockService service, StockModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(
            summary = "Criar estoque",
            description = "Cadastra um novo registro de estoque"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Estoque criado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            )
    })
    @PostMapping
    public ResponseEntity<EntityModel<Stock>> criar(@RequestBody Stock stock) {

        Stock criado = service.salvar(stock);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(criado));
    }

    @Operation(
            summary = "Listar estoques",
            description = "Retorna os registros de estoque cadastrados com paginação e links HATEOAS"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Estoques encontrados"
    )
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Stock>>> listar(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @Parameter(hidden = true) PagedResourcesAssembler<Stock> pagedResourcesAssembler) {

        Page<Stock> pagina = service.listarTodos(pageable);

        return ResponseEntity.ok(pagedResourcesAssembler.toModel(pagina, assembler));
    }

    @Operation(
            summary = "Buscar estoque por ID",
            description = "Busca um estoque específico através do seu ID com links HATEOAS"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estoque encontrado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estoque não encontrado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Stock>> buscarPorId(@PathVariable Long id) {

        return service.buscarPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Atualizar estoque",
            description = "Atualiza os dados de um estoque existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estoque atualizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estoque não encontrado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Stock>> atualizar(
            @PathVariable Long id,
            @RequestBody Stock stock) {

        return service.atualizar(id, stock)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Excluir estoque",
            description = "Remove um estoque do sistema"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Estoque excluído com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estoque não encontrado"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        if (!service.deletar(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
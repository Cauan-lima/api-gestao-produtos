package br.com.cauanproject.controller;

import br.com.cauanproject.assembler.SupplierModelAssembler;
import br.com.cauanproject.entity.Supplier;
import br.com.cauanproject.service.SupplierService;
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
@RequestMapping("/supplier")
@Tag(
        name = "Fornecedores",
        description = "Operações de gerenciamento de fornecedores"
)
public class SupplierController {

    private final SupplierService service;
    private final SupplierModelAssembler assembler;

    public SupplierController(SupplierService service, SupplierModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(
            summary = "Criar fornecedor",
            description = "Cadastra um novo fornecedor"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Fornecedor criado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            )
    })
    @PostMapping
    public ResponseEntity<EntityModel<Supplier>> criar(@RequestBody Supplier supplier) {

        Supplier criado = service.salvar(supplier);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(criado));
    }

    @Operation(
            summary = "Listar fornecedores",
            description = "Retorna os fornecedores cadastrados com paginação e links HATEOAS"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Fornecedores encontrados"
    )
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Supplier>>> listar(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @Parameter(hidden = true) PagedResourcesAssembler<Supplier> pagedResourcesAssembler) {

        Page<Supplier> pagina = service.listarTodos(pageable);

        return ResponseEntity.ok(pagedResourcesAssembler.toModel(pagina, assembler));
    }

    @Operation(
            summary = "Buscar fornecedor por ID",
            description = "Busca um fornecedor específico através do seu ID com links HATEOAS"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Fornecedor encontrado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Fornecedor não encontrado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Supplier>> buscarPorId(@PathVariable Long id) {

        return service.buscarPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Atualizar fornecedor",
            description = "Atualiza os dados de um fornecedor existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Fornecedor atualizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Fornecedor não encontrado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Supplier>> atualizar(
            @PathVariable Long id,
            @RequestBody Supplier supplier) {

        return service.atualizar(id, supplier)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Excluir fornecedor",
            description = "Remove um fornecedor do sistema"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Fornecedor excluído com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Fornecedor não encontrado"
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
package br.com.cauanproject.controller;

import br.com.cauanproject.assembler.BrandModelAssembler;
import br.com.cauanproject.entity.Brand;
import br.com.cauanproject.service.BrandService;
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
@RequestMapping("/brand")
@Tag(
        name = "Marcas",
        description = "Operações de gerenciamento de marcas"
)
public class BrandController {

    private final BrandService service;
    private final BrandModelAssembler assembler;

    public BrandController(BrandService service, BrandModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(
            summary = "Criar marca",
            description = "Cadastra uma nova marca"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Marca criada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            )
    })
    @PostMapping
    public ResponseEntity<EntityModel<Brand>> criar(@RequestBody Brand brand) {

        Brand criada = service.salvar(brand);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(criada));
    }

    @Operation(
            summary = "Listar marcas",
            description = "Retorna as marcas cadastradas com paginação e links HATEOAS"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Marcas encontradas"
    )
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Brand>>> listar(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @Parameter(hidden = true) PagedResourcesAssembler<Brand> pagedResourcesAssembler) {

        Page<Brand> pagina = service.listarTodos(pageable);

        return ResponseEntity.ok(pagedResourcesAssembler.toModel(pagina, assembler));
    }

    @Operation(
            summary = "Buscar marca por ID",
            description = "Busca uma marca específica através do seu ID com links HATEOAS"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Marca encontrada"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Marca não encontrada"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Brand>> buscarPorId(@PathVariable Long id) {

        return service.buscarPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Atualizar marca",
            description = "Atualiza os dados de uma marca existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Marca atualizada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Marca não encontrada"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Brand>> atualizar(
            @PathVariable Long id,
            @RequestBody Brand brand) {

        return service.atualizar(id, brand)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Excluir marca",
            description = "Remove uma marca do sistema"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Marca excluída com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Marca não encontrada"
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
package br.com.cauanproject.controller;

import br.com.cauanproject.assembler.CategoryModelAssembler;
import br.com.cauanproject.entity.Category;
import br.com.cauanproject.service.CategoryService;
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
@RequestMapping("/category")
@Tag(
        name = "Categorias",
        description = "Operações de gerenciamento de categorias"
)
public class CategoryController {

    private final CategoryService service;
    private final CategoryModelAssembler assembler;

    public CategoryController(CategoryService service, CategoryModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    // POST - 201 Created
    @Operation(
            summary = "Criar categoria",
            description = "Cadastra uma nova categoria"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Categoria criada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            )
    })
    @PostMapping
    public ResponseEntity<EntityModel<Category>> criar(@RequestBody Category category) {

        Category categoriaCriada = service.salvar(category);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(categoriaCriada));
    }

    // GET - 200 OK
    @Operation(
            summary = "Listar categorias",
            description = "Retorna as categorias cadastradas com paginação e links HATEOAS"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Categorias encontradas"
    )
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Category>>> listar(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @Parameter(hidden = true) PagedResourcesAssembler<Category> pagedResourcesAssembler) {

        Page<Category> pagina = service.listarTodos(pageable);

        return ResponseEntity.ok(pagedResourcesAssembler.toModel(pagina, assembler));
    }

    // GET /id - 200 ou 404
    @Operation(
            summary = "Buscar categoria por ID",
            description = "Busca uma categoria específica através do seu ID com links HATEOAS"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Categoria encontrada"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoria não encontrada"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Category>> buscarPorId(@PathVariable Long id) {

        return service.buscarPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT - 200 ou 404
    @Operation(
            summary = "Atualizar categoria",
            description = "Atualiza os dados de uma categoria existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Categoria atualizada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoria não encontrada"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Category>> atualizar(
            @PathVariable Long id,
            @RequestBody Category category) {

        return service.atualizar(id, category)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - 204 ou 404
    @Operation(
            summary = "Excluir categoria",
            description = "Remove uma categoria do sistema"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Categoria excluída com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoria não encontrada"
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
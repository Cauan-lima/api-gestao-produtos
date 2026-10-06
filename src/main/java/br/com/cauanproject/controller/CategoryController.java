package br.com.cauanproject.controller;

import br.com.cauanproject.entity.Category;
import br.com.cauanproject.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    // POST - 201 Created
    @PostMapping
    public ResponseEntity<Category> criar(@RequestBody Category category) {

        Category categoriaCriada = service.salvar(category);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoriaCriada);
    }

    // GET - 200 OK
    @GetMapping
    public ResponseEntity<List<Category>> listar() {

        return ResponseEntity.ok(service.listarTodos());
    }

    // GET /id - 200 ou 404
    @GetMapping("/{id}")
    public ResponseEntity<Category> buscarPorId(@PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT - 200 ou 404
    @PutMapping("/{id}")
    public ResponseEntity<Category> atualizar(
            @PathVariable Long id,
            @RequestBody Category category) {

        return service.atualizar(id, category)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - 204 ou 404
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        if (!service.deletar(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
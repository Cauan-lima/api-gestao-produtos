package br.com.cauanproject.controller;

import br.com.cauanproject.entity.Brand;
import br.com.cauanproject.service.BrandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {

    private final BrandService service;

    public BrandController(BrandService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Brand> criar(@RequestBody Brand brand) {

        Brand criada = service.salvar(brand);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(criada);
    }

    @GetMapping
    public ResponseEntity<List<Brand>> listar() {

        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brand> buscarPorId(@PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Brand> atualizar(
            @PathVariable Long id,
            @RequestBody Brand brand) {

        return service.atualizar(id, brand)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        if (!service.deletar(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
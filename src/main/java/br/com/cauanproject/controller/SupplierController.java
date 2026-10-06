package br.com.cauanproject.controller;

import br.com.cauanproject.entity.Supplier;
import br.com.cauanproject.service.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    private final SupplierService service;

    public SupplierController(SupplierService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Supplier> criar(@RequestBody Supplier supplier) {

        Supplier criado = service.salvar(supplier);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(criado);
    }

    @GetMapping
    public ResponseEntity<List<Supplier>> listar() {

        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> buscarPorId(@PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> atualizar(
            @PathVariable Long id,
            @RequestBody Supplier supplier) {

        return service.atualizar(id, supplier)
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
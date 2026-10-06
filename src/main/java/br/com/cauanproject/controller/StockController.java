package br.com.cauanproject.controller;

import br.com.cauanproject.entity.Stock;
import br.com.cauanproject.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockService service;

    public StockController(StockService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Stock> criar(@RequestBody Stock stock) {

        Stock criado = service.salvar(stock);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(criado);
    }

    @GetMapping
    public ResponseEntity<List<Stock>> listar() {

        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> buscarPorId(@PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> atualizar(
            @PathVariable Long id,
            @RequestBody Stock stock) {

        return service.atualizar(id, stock)
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
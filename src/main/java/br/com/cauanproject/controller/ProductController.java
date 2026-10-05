package br.com.cauanproject.controller;

import br.com.cauanproject.entity.Product;
import br.com.cauanproject.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> criarProduto(@RequestBody Product product) {
        Product novoProduto = productService.salvar(product);
        return ResponseEntity.ok(novoProduto);
    }

    @GetMapping
    public ResponseEntity<List<Product>> listarProduto() {
        java.util.List<Product> products = productService.ListarTodos();
        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        productService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> buscarPorId(@PathVariable Long id) {
        Product product = productService.buscarPorId(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> atualizarProduto(
            @PathVariable Long id,
            @RequestBody Product product) {

        Product produtoAtualizado = productService.atualizar(id, product);

        return ResponseEntity.ok(produtoAtualizado);
    }
}

package br.com.cauanproject.service;

import br.com.cauanproject.entity.Product;
import br.com.cauanproject.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product salvar (Product product) {
        return productRepository.save(product);
    }

    public List<Product> ListarTodos(){
        return productRepository.findAll();
    }

    public void deletar(Long id) {
        productRepository.deleteById(id);
    }

    public Product buscarPorId(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product atualizar(Long id, Product product) {
        product.setId(id);
        return productRepository.save(product);
    }
}

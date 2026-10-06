package br.com.cauanproject.service;

import br.com.cauanproject.dto.ProductRequest;
import br.com.cauanproject.entity.Product;
import br.com.cauanproject.repository.BrandRepository;
import br.com.cauanproject.repository.CategoryRepository;
import br.com.cauanproject.repository.ProductRepository;
import br.com.cauanproject.repository.StockRepository;
import br.com.cauanproject.repository.SupplierRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final StockRepository stockRepository;
    private final BrandRepository brandRepository;

    public ProductService(
            ProductRepository repository,
            CategoryRepository categoryRepository,
            SupplierRepository supplierRepository,
            StockRepository stockRepository,
            BrandRepository brandRepository) {

        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.stockRepository = stockRepository;
        this.brandRepository = brandRepository;
    }

    // CREATE
    public Product salvar(ProductRequest request) {

        Product product = new Product();

        product.setNome(request.getNome());
        product.setPreco(request.getPreco());
        product.setQuantidade(request.getQuantidade());

        product.setCategory(
                categoryRepository.findById(request.getCategoryId())
                        .orElseThrow()
        );

        product.setSuppliers(
                supplierRepository.findAllById(request.getSupplierIds())
        );

        product.setStock(
                stockRepository.findById(request.getStockId())
                        .orElseThrow()
        );

        product.setBrand(
                brandRepository.findById(request.getBrandId())
                        .orElseThrow()
        );

        return repository.save(product);
    }

    // READ ALL COM PAGINAÇÃO
    public Page<Product> listarTodos(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Product> listarTodos() {
        return repository.findAll();
    }
    // READ BY ID
    public Optional<Product> buscarPorId(Long id) {
        return repository.findById(id);
    }

    // UPDATE
    public Optional<Product> atualizar(Long id, Product product) {

        Optional<Product> existente = repository.findById(id);

        if (existente.isEmpty()) {
            return Optional.empty();
        }

        Product produto = existente.get();

        produto.setNome(product.getNome());
        produto.setPreco(product.getPreco());
        produto.setQuantidade(product.getQuantidade());
        produto.setCategory(product.getCategory());
        produto.setSuppliers(product.getSuppliers());
        produto.setStock(product.getStock());
        produto.setBrand(product.getBrand());

        return Optional.of(repository.save(produto));
    }

    // DELETE
    public boolean deletar(Long id) {

        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);

        return true;
    }
}
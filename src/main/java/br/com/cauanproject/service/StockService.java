package br.com.cauanproject.service;

import br.com.cauanproject.entity.Stock;
import br.com.cauanproject.repository.StockRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    private final StockRepository repository;

    public StockService(StockRepository repository) {
        this.repository = repository;
    }

    public Stock salvar(Stock stock) {
        return repository.save(stock);
    }

    public Page<Stock> listarTodos(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Stock> listarTodos() {
        return repository.findAll();
    }

    public Optional<Stock> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Optional<Stock> atualizar(Long id, Stock stock) {

        Optional<Stock> existente = repository.findById(id);

        if (existente.isEmpty()) {
            return Optional.empty();
        }

        Stock estoque = existente.get();

        estoque.setQuantidade(stock.getQuantidade());
        estoque.setEstoqueMinimo(stock.getEstoqueMinimo());

        return Optional.of(repository.save(estoque));
    }

    public boolean deletar(Long id) {

        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }
}
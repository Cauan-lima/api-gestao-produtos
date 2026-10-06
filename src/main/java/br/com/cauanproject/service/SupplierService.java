package br.com.cauanproject.service;

import br.com.cauanproject.entity.Supplier;
import br.com.cauanproject.repository.SupplierRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    private final SupplierRepository repository;

    public SupplierService(SupplierRepository repository) {
        this.repository = repository;
    }

    public Supplier salvar(Supplier supplier) {
        return repository.save(supplier);
    }

    public Page<Supplier> listarTodos(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Supplier> listarTodos() {
        return repository.findAll();
    }

    public Optional<Supplier> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Optional<Supplier> atualizar(Long id, Supplier supplier) {

        Optional<Supplier> existente = repository.findById(id);

        if (existente.isEmpty()) {
            return Optional.empty();
        }

        Supplier fornecedor = existente.get();

        fornecedor.setNome(supplier.getNome());
        fornecedor.setEmail(supplier.getEmail());

        return Optional.of(repository.save(fornecedor));
    }

    public boolean deletar(Long id) {

        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }
}
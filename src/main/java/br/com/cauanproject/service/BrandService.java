package br.com.cauanproject.service;

import br.com.cauanproject.entity.Brand;
import br.com.cauanproject.repository.BrandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {

    private final BrandRepository repository;

    public BrandService(BrandRepository repository) {
        this.repository = repository;
    }

    public Brand salvar(Brand brand) {
        return repository.save(brand);
    }

    public Page<Brand> listarTodos(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Brand> listarTodos() {
        return repository.findAll();
    }

    public Optional<Brand> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Optional<Brand> atualizar(Long id, Brand brand) {

        Optional<Brand> existente = repository.findById(id);

        if (existente.isEmpty()) {
            return Optional.empty();
        }

        Brand marca = existente.get();

        marca.setNome(brand.getNome());
        marca.setPais(brand.getPais());

        return Optional.of(repository.save(marca));
    }

    public boolean deletar(Long id) {

        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }
}
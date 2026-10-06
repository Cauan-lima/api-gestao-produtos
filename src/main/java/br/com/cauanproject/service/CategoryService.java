package br.com.cauanproject.service;

import br.com.cauanproject.entity.Category;
import br.com.cauanproject.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Category salvar(Category category) {
        return repository.save(category);
    }

    public Page<Category> listarTodos(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Category> listarTodos() {
        return repository.findAll();
    }

    public Optional<Category> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Optional<Category> atualizar(Long id, Category category) {

        Optional<Category> existente = repository.findById(id);

        if (existente.isEmpty()) {
            return Optional.empty();
        }

        Category categoria = existente.get();

        categoria.setNome(category.getNome());
        categoria.setDescricao(category.getDescricao());

        return Optional.of(repository.save(categoria));
    }

    public boolean deletar(Long id) {

        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }
}
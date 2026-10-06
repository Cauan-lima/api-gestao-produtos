package br.com.cauanproject.repository;

import br.com.cauanproject.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
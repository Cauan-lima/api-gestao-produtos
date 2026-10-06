package br.com.cauanproject.repository;

import br.com.cauanproject.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
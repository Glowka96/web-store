package com.example.portfolio.webstorespring.repositories.products;

import com.example.portfolio.webstorespring.models.entities.products.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
}

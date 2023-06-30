package com.example.porfolio.webstorespring.repositories.products;

import com.example.porfolio.webstorespring.model.entity.products.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
}

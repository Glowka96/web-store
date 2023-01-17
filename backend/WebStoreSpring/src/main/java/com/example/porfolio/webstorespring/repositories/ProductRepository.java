package com.example.porfolio.webstorespring.repositories;

import com.example.porfolio.webstorespring.model.entity.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

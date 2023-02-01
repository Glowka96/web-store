package com.example.porfolio.webstorespring.repositories;

import com.example.porfolio.webstorespring.model.entity.products.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}


package com.example.porfolio.webstorespring.repositories;

import com.example.porfolio.webstorespring.model.entity.products.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

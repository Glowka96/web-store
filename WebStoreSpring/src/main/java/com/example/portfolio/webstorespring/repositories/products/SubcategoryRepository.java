package com.example.portfolio.webstorespring.repositories.products;

import com.example.portfolio.webstorespring.model.entity.products.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
}

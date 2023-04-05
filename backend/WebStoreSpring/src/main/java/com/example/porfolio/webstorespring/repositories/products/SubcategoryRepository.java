package com.example.porfolio.webstorespring.repositories.products;

import com.example.porfolio.webstorespring.model.entity.products.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
}

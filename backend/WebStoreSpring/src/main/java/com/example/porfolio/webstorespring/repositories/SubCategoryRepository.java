package com.example.porfolio.webstorespring.repositories;

import com.example.porfolio.webstorespring.model.entity.products.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
}

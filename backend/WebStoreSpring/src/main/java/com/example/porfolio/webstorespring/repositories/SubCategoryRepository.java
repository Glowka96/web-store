package com.example.porfolio.webstorespring.repositories;

import com.example.porfolio.webstorespring.model.entity.products.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    Optional<SubCategory> findByName(String name);

    void deleteByIdName(String subCategoryName);
}

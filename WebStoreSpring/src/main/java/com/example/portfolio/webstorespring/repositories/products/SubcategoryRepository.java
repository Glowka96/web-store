package com.example.portfolio.webstorespring.repositories.products;

import com.example.portfolio.webstorespring.models.entities.products.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {


    @Query("""
        SELECT s FROM Subcategory s
        WHERE s.name IN :names
""")
    Set<Subcategory> findAllByNames(@Param("names") Set<String> names);
}

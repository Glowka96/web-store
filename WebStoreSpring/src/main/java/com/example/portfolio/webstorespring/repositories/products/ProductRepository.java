package com.example.portfolio.webstorespring.repositories.products;

import com.example.portfolio.webstorespring.model.entity.products.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Page<Product>> findProductBySubcategory_Id(Long subcategory_id, Pageable pageable);
    Long countProductBySubcategory_Id(Long subcategoryId);

    Optional<Page<Product>> searchProductByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrProducerName(String name,
                                                                                             String description,
                                                                                             String producer_name,
                                                                                             Pageable pageable);

    Long countProductByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrProducerName(String name,
                                                                         String description,
                                                                         String producer_name);
}
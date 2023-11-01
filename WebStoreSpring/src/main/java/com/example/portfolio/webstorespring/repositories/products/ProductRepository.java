package com.example.portfolio.webstorespring.repositories.products;

import com.example.portfolio.webstorespring.model.entity.products.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(value = "product-with-producer-and-price-promotion-entity-graph")
    Optional<Page<Product>> findProductsBySubcategory_Id(Long subcategoryId, Pageable pageable);

    Long countProductsBySubcategory_Id(Long subcategoryId);

    @Query(value = """
            SELECT p_1 FROM Product p_1
            INNER JOIN p_1.producer p_2
            WHERE LOWER(p_1.name) LIKE LOWER(CONCAT('%', :text, '%'))
            OR LOWER(p_1.description) LIKE LOWER(CONCAT('%', :text, '%'))
            OR LOWER(p_2.name) LIKE LOWER(CONCAT('%', :text, '%'))
            """)
    @EntityGraph(value = "product-with-producer-and-price-promotion-entity-graph")
    Optional<Page<Product>> searchProductsByEnteredText(@Param("text") String text,
                                                        Pageable pageable);


    @Query(value = """
            SELECT COUNT(p_1) AS id FROM Product p_1
            INNER JOIN p_1.producer p_2
            WHERE LOWER(p_1.name) LIKE LOWER(CONCAT('%', :text, '%'))
            OR LOWER(p_1.description) LIKE LOWER(CONCAT('%', :text, '%'))
            OR LOWER(p_2.name) LIKE LOWER(CONCAT('%', :text, '%'))
            """)
    Long countProductsByEnteredText(@Param("text") String text);
}

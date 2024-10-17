package com.example.portfolio.webstorespring.repositories.products;

import com.example.portfolio.webstorespring.model.entity.products.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    @Query("""
        SELECT d FROM Discount d
        LEFT JOIN FETCH d.subcategories
        WHERE d.code = :code
""")
    Optional<Discount> findByCode(@Param("code") String code);

    boolean existsByCode(String code);
}

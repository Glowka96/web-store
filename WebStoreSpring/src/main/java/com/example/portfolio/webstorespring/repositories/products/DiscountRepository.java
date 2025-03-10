package com.example.portfolio.webstorespring.repositories.products;

import com.example.portfolio.webstorespring.models.entities.products.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    @Query("""
            SELECT d FROM Discount d
            LEFT JOIN FETCH d.subcategories
            WHERE d.code = :code
            AND (d.quantity > 0 AND (d.endDate IS NULL OR d.endDate >= CURRENT_DATE))
            """)
    Optional<Discount> findByCode(@Param("code") String code);

    boolean existsByCode(String code);

    @Modifying
    @Query("""
            DELETE FROM Discount d
            WHERE d.quantity = 0
            OR (d.endDate  IS NOT NULL AND d.endDate < CURRENT_DATE)
            """)
    void deleteZeroQuantityOrExpiredDiscounts();
}

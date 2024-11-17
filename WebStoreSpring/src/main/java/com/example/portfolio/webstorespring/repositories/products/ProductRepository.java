package com.example.portfolio.webstorespring.repositories.products;

import com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
            SELECT NEW com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO(
                    p.id, p.name, p.imageUrl, p.quantity, t.name, s.id, p.price, prom_1.promotionPrice, MIN(prom_2.promotionPrice), prom_1.endDate,
                p.description, t.name)
            FROM Product p
            INNER JOIN p.subcategory s
            INNER JOIN p.producer pr
            INNER JOIN p.type t
            LEFT JOIN p.promotions prom_1 ON (CURRENT_TIMESTAMP BETWEEN prom_1.startDate AND prom_1.endDate)
            LEFT JOIN p.promotions prom_2 ON (prom_2.endDate >= :date30DaysAgo AND prom_1.id IS NOT NULL)
            WHERE p.id = :productId AND p.quantity > 0
            GROUP BY p.id, p.name, p.imageUrl, p.quantity, t.name, p.price, prom_1.promotionPrice, prom_1.endDate, p.description, p.producer.name
            """)
    ProductWithProducerAndPromotionDTO findById(@Param("productId") Long productId,
                                                @Param("date30DaysAgo") LocalDateTime date30DaysAgo);


    @Query("""
            SELECT NEW com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO(
                p.id, p.name, p.imageUrl, p.quantity,s.id, p.price, prom_1.promotionPrice, MIN(prom_2.promotionPrice))
            FROM Product p
            INNER JOIN p.subcategory s
            LEFT JOIN p.promotions prom_1 ON (CURRENT_TIMESTAMP BETWEEN prom_1.startDate AND prom_1.endDate)
            LEFT JOIN p.promotions prom_2 ON (prom_2.endDate >= :date30DaysAgo AND prom_1.id IS NOT NULL)
            WHERE p.subcategory.id = :subcategoryId AND p.quantity > 0
            GROUP BY p.id, p.name, p.imageUrl, p.quantity, p.price, prom_1.promotionPrice
            """)
    Optional<Page<ProductWithPromotionDTO>> findBySubcategoryId(@Param("subcategoryId") Long subcategoryId,
                                                                @Param("date30DaysAgo") LocalDateTime date30DaysAgo,
                                                                Pageable pageable);

    @Query(value = """ 
            SELECT NEW com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO(
                                 p.id, p.name, p.imageUrl, p.quantity, s.id, p.price, prom_1.promotionPrice, MIN(prom_2.promotionPrice))
            FROM Product p
            INNER JOIN p.subcategory s
            INNER JOIN p.producer pr
            INNER JOIN p.type t
            LEFT JOIN p.promotions prom_1 ON (CURRENT_TIMESTAMP BETWEEN prom_1.startDate AND prom_1.endDate)
            LEFT JOIN p.promotions prom_2 ON (prom_2.endDate >= :date30DaysAgo AND prom_1.id IS NOT NULL)
            WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :text, '%'))
            OR LOWER(p.description) LIKE LOWER(CONCAT('%', :text, '%'))
            OR LOWER(pr.name) LIKE LOWER(CONCAT('%', :text, '%'))
            OR LOWER(t.name) LIKE LOWER(CONCAT('%', :text, '%')))
            AND p.quantity > 0
            GROUP BY p.id, p.name, p.imageUrl, p.quantity, t.name, p.price, prom_1.promotionPrice
            """)
    Optional<Page<ProductWithPromotionDTO>> searchByEnteredText(@Param("text") String text,
                                                                @Param("date30DaysAgo") LocalDateTime date30DaysAgo,
                                                                Pageable pageable);

    @Query(value = """
            SELECT NEW com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO(
                                 p.id, p.name, p.imageUrl, p.quantity, s.id, p.price, prom_1.promotionPrice, MIN(prom_2.promotionPrice))
            FROM Product p
            INNER JOIN p.subcategory s
            LEFT JOIN p.promotions prom_1
            LEFT JOIN p.promotions prom_2
            WHERE (CURRENT_TIMESTAMP BETWEEN prom_1.startDate AND prom_1.endDate)
            AND prom_2.endDate >= :date30DaysAgo
            AND p.quantity > 0
            GROUP BY p.id, p.name, p.imageUrl, p.quantity, p.price, prom_1.promotionPrice, prom_1.endDate
            """)
    Optional<Page<ProductWithPromotionDTO>> findPromotionProducts(@Param("date30DaysAgo") LocalDateTime date30DaysAgo,
                                                                  Pageable pageable);

    @Query(value = """
            SELECT NEW com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO(
                                 p.id, p.name, p.imageUrl, p.quantity, s.id, p.price, prom_1.promotionPrice, MIN(prom_2.promotionPrice))
            FROM Product p
            INNER JOIN p.subcategory s
            LEFT JOIN p.promotions prom_1 ON (CURRENT_TIMESTAMP BETWEEN prom_1.startDate AND prom_1.endDate)
            LEFT JOIN p.promotions prom_2 ON (prom_2.endDate >= :date30DaysAgo AND prom_1.id IS NOT NULL)
            WHERE p.createdAt >= :date30DaysAgo
            AND p.quantity > 0
            GROUP BY p.id, p.name, p.imageUrl, p.quantity, p.price, prom_1.promotionPrice
                     """)
    Optional<Page<ProductWithPromotionDTO>> findNewProducts(@Param("date30DaysAgo") LocalDateTime date30DaysAgo,
                                                            Pageable pageable);

    @Query("""
            SELECT p FROM Product p
            LEFT JOIN FETCH p.promotions prom
            WHERE p.id = :productId
            AND (prom IS NULL OR (CURRENT_TIMESTAMP BETWEEN prom.startDate AND prom.endDate))
            """)
    Optional<Product> findWithPromotionById(@Param("productId") Long productId);

    @Query("""
            SELECT p FROM Product p
            LEFT JOIN FETCH p.promotions prom
            LEFT JOIN FETCH p.subcategory s
            WHERE p.id IN :productIds
            AND (prom IS NULL OR (CURRENT_TIMESTAMP BETWEEN prom.startDate AND prom.endDate))
            """)
    List<Product> findWithPromotionByIds(@Param("productIds") List<Long> productIds);
}

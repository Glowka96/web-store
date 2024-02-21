package com.example.portfolio.webstorespring.repositories.products;

import com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
            SELECT NEW com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO(
                    p.id, p.name, p.imageUrl, p.quantity, pt.name, p.price, ppp_1.promotionPrice, MIN(ppp_2.promotionPrice), ppp_1.endDate,
                p.description, p.producer.name)
            FROM Product p
            INNER JOIN p.producer pr
            INNER JOIN p.type pt
            LEFT JOIN p.pricePromotions ppp_1 ON (CURRENT_DATE BETWEEN ppp_1.startDate AND ppp_1.endDate)
            LEFT JOIN p.pricePromotions ppp_2 ON (ppp_2.endDate >= :date30DaysAgo AND ppp_1.id IS NOT NULL)
            WHERE p.id = :productId AND p.quantity > 0
            """)
    ProductWithProducerAndPromotionDTO findProductById(@Param("productId") Long productId,
                                                       @Param("date30DaysAgo") Date date30DaysAgo);

    @Query("""
            SELECT NEW com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO(
                p.id, p.name, p.imageUrl, p.quantity, pt.name, p.price, ppp_1.promotionPrice, MIN(ppp_2.promotionPrice), ppp_1.endDate)
            FROM Product p
            INNER JOIN p.type pt
            LEFT JOIN p.pricePromotions ppp_1 ON (CURRENT_DATE BETWEEN ppp_1.startDate AND ppp_1.endDate)
            LEFT JOIN p.pricePromotions ppp_2 ON (ppp_2.endDate >= :date30DaysAgo AND ppp_1.id IS NOT NULL)
            WHERE p.subcategory.id = :subcategoryId AND p.quantity > 0
            GROUP BY p.id
                       """)
    Optional<Page<ProductWithPromotionDTO>> findProductsBySubcategory_Id(@Param("subcategoryId") Long subcategoryId,
                                                                         @Param("date30DaysAgo") Date date30DaysAgo,
                                                                         Pageable pageable);

    @Query(value = """ 
            SELECT NEW com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO(
                                 p.id, p.name, p.imageUrl, p.quantity, pt.name, p.price, ppp_1.promotionPrice, MIN(ppp_2.promotionPrice), ppp_1.endDate)
            FROM Product p
            INNER JOIN p.producer pr
            INNER JOIN p.type pt
            LEFT JOIN p.pricePromotions ppp_1 ON (CURRENT_DATE BETWEEN ppp_1.startDate AND ppp_1.endDate)
            LEFT JOIN p.pricePromotions ppp_2 ON (ppp_2.endDate >= :date30DaysAgo AND ppp_1.id IS NOT NULL)
            WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :text, '%'))
            OR LOWER(p.description) LIKE LOWER(CONCAT('%', :text, '%'))
            OR LOWER(pr.name) LIKE LOWER(CONCAT('%', :text, '%')))
            AND p.quantity > 0
            GROUP BY p.id
                       """)
    Optional<Page<ProductWithPromotionDTO>> searchProductsByEnteredText(@Param("text") String text,
                                                                        @Param("date30DaysAgo") Date date30DaysAgo,
                                                                        Pageable pageable);

    @Query(value = """
            SELECT NEW com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO(
                                 p.id, p.name, p.imageUrl, p.quantity, pt.name, p.price, ppp_1.promotionPrice, MIN(ppp_2.promotionPrice), ppp_1.endDate)
            FROM Product p
            INNER JOIN p.type pt
            LEFT JOIN p.pricePromotions ppp_1
            LEFT JOIN p.pricePromotions ppp_2
            WHERE (CURRENT_DATE BETWEEN ppp_1.startDate AND ppp_1.endDate)
            AND ppp_2.endDate >= :date30DaysAgo
            AND p.quantity > 0
            GROUP BY p.id
                        """)
    Optional<Page<ProductWithPromotionDTO>> findPromotionProducts(@Param("date30DaysAgo") Date date30DaysAgo,
                                                                  Pageable pageable);

    @Query(value = """
                        SELECT NEW com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO(
                                             p.id, p.name, p.imageUrl, p.quantity, pt.name, p.price, ppp_1.promotionPrice, MIN(ppp_2.promotionPrice), ppp_1.endDate)
                        FROM Product p
                        INNER JOIN p.type pt
                        LEFT JOIN p.pricePromotions ppp_1 ON (CURRENT_DATE BETWEEN ppp_1.startDate AND ppp_1.endDate)
                        LEFT JOIN p.pricePromotions ppp_2 ON (ppp_2.endDate >= :date30DaysAgo AND ppp_1.id IS NOT NULL)
                        WHERE p.dateOfCreation >= :date30DaysAgo
                        AND p.quantity > 0
                        GROUP BY p.id
            """)
    Optional<Page<ProductWithPromotionDTO>> findNewProducts(@Param("date30DaysAgo") Date date30DaysAgo,
                                                            Pageable pageable);

    @Query("""
            SELECT p FROM Product p
            LEFT JOIN p.pricePromotions pp
            ON (CURRENT_DATE BETWEEN pp.startDate AND pp.endDate)
            WHERE p.id = :productId
            """)
    Optional<Product> findProductByIdWithPromotion(@Param("productId") Long productId);
}

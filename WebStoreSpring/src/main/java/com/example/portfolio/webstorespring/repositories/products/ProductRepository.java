package com.example.portfolio.webstorespring.repositories.products;

import com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionAndLowestPriceDTO;
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
            SELECT NEW com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionAndLowestPriceDTO(
                p.id, p.name, p.imageUrl, p.quantity, p.type, p.price, ppp_1.promotionPrice, MIN(ppp_2.promotionPrice), ppp_1.endDate)
            FROM Product p
            LEFT JOIN p.pricePromotions ppp_1 ON (CURRENT_DATE BETWEEN ppp_1.startDate AND ppp_1.endDate)
            LEFT JOIN p.pricePromotions ppp_2 ON (ppp_2.endDate >= :date30DaysAgo AND ppp_1.id IS NOT NULL)
            WHERE p.subcategory.id = :subcategoryId
            GROUP BY p.id
                       """)
    Optional<Page<ProductWithPromotionAndLowestPriceDTO>> findProductsBySubcategory_Id(@Param("subcategoryId") Long subcategoryId,
                                                                                       @Param("date30DaysAgo") Date date30DaysAgo,
                                                                                       Pageable pageable);

    @Query(value = """ 
            SELECT NEW com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionAndLowestPriceDTO(
                                 p.id, p.name, p.imageUrl, p.quantity, p.type, p.price, ppp_1.promotionPrice, MIN(ppp_2.promotionPrice), ppp_1.endDate)
            FROM Product p
            INNER JOIN p.producer pr
            LEFT JOIN p.pricePromotions ppp_1 ON (CURRENT_DATE BETWEEN ppp_1.startDate AND ppp_1.endDate)
            LEFT JOIN p.pricePromotions ppp_2 ON (ppp_2.endDate >= :date30DaysAgo AND ppp_1.id IS NOT NULL)
            WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :text, '%'))
            OR LOWER(p.description) LIKE LOWER(CONCAT('%', :text, '%'))
            OR LOWER(pr.name) LIKE LOWER(CONCAT('%', :text, '%')))
            GROUP BY p.id
                       """)
    Optional<Page<ProductWithPromotionAndLowestPriceDTO>> searchProductsByEnteredText(@Param("text") String text,
                                                                                      @Param("date30DaysAgo") Date date30DaysAgo,
                                                                                      Pageable pageable);

    @Query(value = """
            SELECT NEW com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionAndLowestPriceDTO(
                                 p.id, p.name, p.imageUrl, p.quantity, p.type, p.price, ppp_1.promotionPrice, MIN(ppp_2.promotionPrice), ppp_1.endDate)
            FROM Product p
            Left JOIN p.pricePromotions ppp_1
            LEFT JOIN p.pricePromotions ppp_2
            WHERE (CURRENT_DATE between ppp_1.startDate AND ppp_1.endDate)
            AND ppp_2.endDate >= :date30DaysAgo
            GROUP BY p.id
                        """)
    Optional<Page<ProductWithPromotionAndLowestPriceDTO>> findPromotionProducts(@Param("date30DaysAgo") Date date30DaysAgo,
                                                                                Pageable pageable);
}

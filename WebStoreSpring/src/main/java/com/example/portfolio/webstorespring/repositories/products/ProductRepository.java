package com.example.portfolio.webstorespring.repositories.products;

import com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionAndLowestPriceDTO;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
            SELECT NEW com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionAndLowestPriceDTO(
                      p.id, p.name, p.imageUrl, p.quantity, p.type, ppp_1.endDate, ppp_1.promotionPrice, MIN(ppp_2.promotionPrice))
                      FROM Product p
                      LEFT JOIN p.pricePromotions ppp_1
                      LEFT JOIN ProductPricePromotion ppp_2 ON ppp_2.product.id = p.id
                      WHERE p.subcategory.id = :subcategoryId
                      AND (ppp_2.endDate >= CURRENT_DATE - 30 DAY OR ppp_2.id IS NULL)
                      AND (ppp_1.startDate <= CURRENT_DATE AND ppp_1.endDate >= CURRENT_DATE OR ppp_1.id IS NULL)
                      GROUP BY p.id
                      ORDER BY p.id
                   """)
    Optional<Page<ProductWithPromotionAndLowestPriceDTO>> findProductsBySubcategory_Id(@Param("subcategoryId") Long subcategoryId, Pageable pageable);

    @Query(value = """ 
            SELECT NEW com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionAndLowestPriceDTO(
                                 p.id, p.name, p.imageUrl, p.quantity, p.type, ppp_1.endDate, ppp_1.promotionPrice, MIN(ppp_2.promotionPrice))
                       FROM Product p
                       INNER JOIN p.producer pr
                       LEFT JOIN p.pricePromotions ppp_1
                       LEFT JOIN ProductPricePromotion ppp_2 ON ppp_2.product.id = p.id
                       WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :text, '%'))
                       OR LOWER(p.description) LIKE LOWER(CONCAT('%', :text, '%'))
                       OR LOWER(pr.name) LIKE LOWER(CONCAT('%', :text, '%')))
                       AND (ppp_2.endDate >= CURRENT_DATE - 30 DAY OR ppp_2.id IS NULL)
                       AND (ppp_1.startDate <= CURRENT_DATE AND ppp_1.endDate >= CURRENT_DATE OR ppp_1.id IS NULL)
                       GROUP BY p.id
                       ORDER BY p.id
                       """)
    Optional<Page<ProductWithPromotionAndLowestPriceDTO>> searchProductsByEnteredText(@Param("text") String text,
                                                        Pageable pageable);
}

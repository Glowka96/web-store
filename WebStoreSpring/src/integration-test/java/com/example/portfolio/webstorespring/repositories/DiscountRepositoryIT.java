package com.example.portfolio.webstorespring.repositories;

import com.example.portfolio.webstorespring.ContainersConfig;
import com.example.portfolio.webstorespring.buildhelpers.products.SubcategoryBuilderHelper;
import com.example.portfolio.webstorespring.model.entity.products.Discount;
import com.example.portfolio.webstorespring.model.entity.products.Subcategory;
import com.example.portfolio.webstorespring.repositories.products.DiscountRepository;
import com.example.portfolio.webstorespring.repositories.products.SubcategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import({ContainersConfig.class})
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DiscountRepositoryIT {

    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private SubcategoryRepository subcategoryRepository;
    private static final String DISCOUNT_CODE = "test";

    @BeforeEach
    void initTestData() {
        Subcategory subcategory = subcategoryRepository.save(SubcategoryBuilderHelper.createSubcategory());
        subcategoryRepository.save(Subcategory.builder().name("other").build());
        discountRepository.save(Discount.builder()
                        .code(DISCOUNT_CODE)
                        .discountRate(BigDecimal.valueOf(0.1))
                        .quantity(10L)
                        .subcategories(Set.of(subcategory))
                .build());
    }

    @Test
    void shouldGetDiscountCodeByCode() {
        Optional<Discount> optionalDiscount = discountRepository.findByCode(DISCOUNT_CODE);

        assertTrue(optionalDiscount.isPresent());
        assertEquals(DISCOUNT_CODE, optionalDiscount.get().getCode());
        assertEquals(1, optionalDiscount.get().getSubcategories().size());
    }
}

package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.DiscountIsInvalid;
import com.example.portfolio.webstorespring.model.dto.products.request.DiscountRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.DiscountAdminResponse;
import com.example.portfolio.webstorespring.model.dto.products.response.DiscountUserResponse;
import com.example.portfolio.webstorespring.model.entity.products.Discount;
import com.example.portfolio.webstorespring.repositories.products.DiscountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final SubcategoryService subcategoryService;
    private final DiscountRandomizer discountRandomizer;

    public DiscountUserResponse getByCode(String code) {
        return DiscountUserResponse.mapToResponse(
                findByCode(code)
        );
    }

    @Transactional
    public DiscountAdminResponse save(DiscountRequest discountRequest) {
        log.info("Saving discount from request: {}", discountRequest);
        Discount discount = Discount.builder()
                .code(getCode(discountRequest))
                .discountRate(discountRequest.discountRate())
                .quantity(discountRequest.quantity())
                .endDate(discountRequest.endDate())
                .subcategories(subcategoryService.findAllByNames(discountRequest.subcategoryNames()))
                .build();

        discountRepository.save(discount);
        log.info("Saved discount.");
        return DiscountAdminResponse.mapToResponse(discount);
    }

    @Transactional
    public void deleteUsedOrExpiredDiscount() {
        log.info("Deleting used or expired discount.");
        discountRepository.deleteZeroQuantityOrExpiredDiscounts();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Discount applyByCode(String code) {
        Discount discount = findByCode(code);
        log.debug("Decreasing discount quantity.");
        discount.setQuantity(discount.getQuantity() - 1);
        log.info("Returning discount.");
        return discount;
    }

    private Discount findByCode(String code) {
        log.info("Fetching discount for code: {}", code);
        return discountRepository.findByCode(code).orElseThrow(DiscountIsInvalid::new);
    }

    private String getCode(DiscountRequest request) {
        return request.code() != null
                ? request.code()
                : discountRandomizer.generateUniqueCode();
    }


}

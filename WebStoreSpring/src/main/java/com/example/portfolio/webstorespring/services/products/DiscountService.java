package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.DiscountIsInvalid;
import com.example.portfolio.webstorespring.model.dto.products.request.DiscountRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.DiscountAdminResponse;
import com.example.portfolio.webstorespring.model.dto.products.response.DiscountUserResponse;
import com.example.portfolio.webstorespring.model.entity.products.Discount;
import com.example.portfolio.webstorespring.repositories.products.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final SubcategoryService subcategoryService;
    private static final Random random = new Random();

    public DiscountUserResponse getDiscountByDiscountCode(String code) {
        return DiscountUserResponse.mapToResponse(
                findDiscountByCode(code)
        );
    }

    @Transactional
    public DiscountAdminResponse saveDiscount(DiscountRequest discountRequest) {
        Discount discount = Discount.builder()
                .code(getCode(discountRequest))
                .discountRate(discountRequest.discountRate())
                .quantity(discountRequest.quantity())
                .endDate(discountRequest.endDate())
                .subcategories(subcategoryService.findAllSubcategoryByNames(discountRequest.subcategoryNames()))
                .build();

        discountRepository.save(discount);
        return DiscountAdminResponse.mapToResponse(discount);
    }

    @Transactional
    public void deleteUsedOrExpiredDiscount() {
        discountRepository.deleteZeroQuantityOrExpiredDiscounts();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Discount useDiscountByCode(String code) {
        Discount discount = findDiscountByCode(code);
        discount.setQuantity(discount.getQuantity() - 1);
        discountRepository.save(discount);
        return discount;
    }

    private Discount findDiscountByCode(String code) {
        return discountRepository.findByCode(code).orElseThrow(DiscountIsInvalid::new);
    }

    private String getCode(DiscountRequest discountRequest) {
        return discountRequest.code() != null ? discountRequest.code() : generateUniqueCode();
    }

    private String generateUniqueCode() {
        String code = generateCode();
        return discountRepository.existsByCode(code) ? generateUniqueCode() : code;
    }

    private static String generateCode() {
        return random.ints(48, 123)
                .filter(num -> (num < 58 || num > 64) && (num < 91 || num > 96))
                .limit(10)
                .mapToObj(c -> (char) c).collect(StringBuffer::new, StringBuffer::append, StringBuffer::append)
                .toString();
    }
}

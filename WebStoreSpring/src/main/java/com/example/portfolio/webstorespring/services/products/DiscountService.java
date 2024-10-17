package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.DiscountIsInvalid;
import com.example.portfolio.webstorespring.model.dto.products.request.DiscountRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.DiscountAdminResponse;
import com.example.portfolio.webstorespring.model.dto.products.response.DiscountUserResponse;
import com.example.portfolio.webstorespring.model.entity.products.Discount;
import com.example.portfolio.webstorespring.repositories.products.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
                discountRepository.findByCode(code).orElseThrow(DiscountIsInvalid::new)
        );
    }

    @Transactional
    public DiscountAdminResponse saveDiscount(DiscountRequest discountRequest) {
        String code;
        if(discountRequest.code() != null) {
            code = discountRequest.code();
        } else {
            code = generateUniqueCode();
        }

        Discount discount = Discount.builder()
                .code(code)
                .discountRate(discountRequest.discountRate())
                .quantity(discountRequest.quantity())
                .endDate(discountRequest.endDate())
                .subcategories(subcategoryService.findAllSubcategoryByNames(discountRequest.subcategoryNames()))
                .build();

        discountRepository.save(discount);
        return DiscountAdminResponse.mapToResponse(discount);
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = generateCode();
        } while (discountRepository.existsByCode(code));
        return code;
    }

    private static String generateCode() {
        return random.ints(48, 123)
                .filter(num -> (num < 58 || num > 64) && (num < 91 || num > 96))
                .limit(10)
                .mapToObj(c -> (char) c).collect(StringBuffer::new, StringBuffer::append, StringBuffer::append)
                .toString();
    }
}

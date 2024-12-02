package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.repositories.products.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class DiscountRandomizer {

    private final Random random;
    private final DiscountRepository discountRepository;

    String generateUniqueCode() {
        String code = generateCode();
        return discountRepository.existsByCode(code) ? generateUniqueCode() : code;
    }

    private String generateCode() {
        return random.ints(48, 123)
                .filter(num -> (num < 58 || num > 64) && (num < 91 || num > 96))
                .limit(10)
                .mapToObj(c -> (char) c).collect(StringBuffer::new, StringBuffer::append, StringBuffer::append)
                .toString();
    }
}

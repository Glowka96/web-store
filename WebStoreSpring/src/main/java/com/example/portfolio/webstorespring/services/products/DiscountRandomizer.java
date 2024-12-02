package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.repositories.products.DiscountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscountRandomizer {

    private final Random random;
    private final DiscountRepository discountRepository;

    String generateUniqueCode() {
        log.info("Generating unique code.");
        String code = generateCode();
        return discountRepository.existsByCode(code) ? generateUniqueCode() : code;
    }

    private String generateCode() {
        log.info("Generating code.");
        return random.ints(48, 123)
                .filter(num -> (num < 58 || num > 64) && (num < 91 || num > 96))
                .limit(10)
                .mapToObj(c -> (char) c).collect(StringBuffer::new, StringBuffer::append, StringBuffer::append)
                .toString();
    }
}

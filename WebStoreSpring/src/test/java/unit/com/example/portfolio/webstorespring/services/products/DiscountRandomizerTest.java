package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.repositories.products.DiscountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscountRandomizerTest {

    @Mock
    private Random random;

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private DiscountRandomizer underTest;

    @Test
    void generateCode_shouldReturn10CharacterString() {
        when(random.ints(48, 123))
                .thenReturn(java.util.stream.IntStream.of(65, 66, 67, 68, 69, 70, 71, 72, 73, 74));

        String code = underTest.generateUniqueCode();

        assertNotNull(code);
        assertEquals(10, code.length());
        assertThat(code).matches("^[a-zA-Z0-9]{5,10}$");
    }

    @Test
    void generateUniqueCode_shouldRetryIfCodeExists() {
        String code1 = "ABCDEFGHIJ";
        String code2 = "KLMNOPQRST";

        when(random.ints(48, 123))
                .thenReturn(java.util.stream.IntStream.of(65, 66, 67, 68, 69, 70, 71, 72, 73, 74))
                .thenReturn(java.util.stream.IntStream.of(75, 76, 77, 78, 79, 80, 81, 82, 83, 84));

        when(discountRepository.existsByCode(code1)).thenReturn(true);
        when(discountRepository.existsByCode(code2)).thenReturn(false);

        String resultCode = underTest.generateUniqueCode();

        assertEquals(code2, resultCode);

        verify(discountRepository).existsByCode(code1);
        verify(discountRepository).existsByCode(code2);
    }
}

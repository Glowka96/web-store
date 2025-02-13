package com.example.portfolio.webstorespring.services.tokens.removals;

import com.example.portfolio.webstorespring.models.entity.tokens.removals.ProductRemovalToken;
import com.example.portfolio.webstorespring.repositories.tokens.removals.RemovalTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.tokens.removals.ProductRemovalTokenBuilderHelper.createProductRemovalTokenWithEnabledSubscriber;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductRemovalTokenServiceTest {

    @Mock
    private RemovalTokenRepository<ProductRemovalToken> removalTokenRepository;

    @InjectMocks
    private ProductRemovalTokenService underTest;

    private static final String TOKEN_VALUE = "token123";

    @Test
    void shouldGetByToken() {
        ProductRemovalToken removalToken = createProductRemovalTokenWithEnabledSubscriber();
        given(removalTokenRepository.findByToken(anyString())).willReturn(Optional.of(removalToken));

        ProductRemovalToken result = underTest.getByToken(TOKEN_VALUE);

        assertNotNull(result);
        assertEquals(TOKEN_VALUE, result.getToken());
        verify(removalTokenRepository, times(1)).findByToken(anyString());
    }

    @Test
    void shouldSave() {
        ProductRemovalToken removalToken = createProductRemovalTokenWithEnabledSubscriber();
        given(removalTokenRepository.save(any(ProductRemovalToken.class))).willReturn(removalToken);

        ProductRemovalToken result = underTest.save(removalToken.getSubscriber());

        assertNotNull(result);
        assertEquals(removalToken.getSubscriber(), result.getSubscriber());
        verify(removalTokenRepository, times(1)).save(any(ProductRemovalToken.class));
    }

    @Test
    void shouldDeleteByToken() {
        underTest.deleteByToken(TOKEN_VALUE);
        verify(removalTokenRepository, times(1)).deleteByToken(TOKEN_VALUE);
    }
}
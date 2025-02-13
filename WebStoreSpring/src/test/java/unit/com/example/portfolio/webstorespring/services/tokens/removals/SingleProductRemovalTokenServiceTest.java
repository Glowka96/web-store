package com.example.portfolio.webstorespring.services.tokens.removals;

import com.example.portfolio.webstorespring.models.entity.tokens.removals.SingleProductRemovalToken;
import com.example.portfolio.webstorespring.repositories.tokens.removals.SingleProductRemovalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.tokens.removals.SingleProductRemovalTokenBuilderHelper.createSingleProductRemovalTokenWithEnabledSubscriber;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
class SingleProductRemovalTokenServiceTest {

    @Mock
    private SingleProductRemovalRepository removalTokenRepository;

    @InjectMocks
    private SingleProductRemovalTokenService underTest;

    private static final String TOKEN_VALUE = "token123";

    @Test
    void shouldGetByToken() {
        SingleProductRemovalToken removalToken = createSingleProductRemovalTokenWithEnabledSubscriber();
        given(removalTokenRepository.findByToken(anyString())).willReturn(Optional.of(removalToken));

        SingleProductRemovalToken result = underTest.getByToken(TOKEN_VALUE);

        assertNotNull(result);
        assertEquals(TOKEN_VALUE, result.getToken());
        verify(removalTokenRepository, times(1)).findByToken(anyString());
    }

    @Test
    void shouldSave() {
        SingleProductRemovalToken removalToken = createSingleProductRemovalTokenWithEnabledSubscriber();
        given(removalTokenRepository.save(any(SingleProductRemovalToken.class))).willReturn(removalToken);

        SingleProductRemovalToken result = underTest.save(removalToken.getSubscriber(), 1L);

        assertNotNull(result);
        assertEquals(removalToken.getSubscriber(), result.getSubscriber());
        verify(removalTokenRepository, times(1)).save(any(SingleProductRemovalToken.class));
    }

    @Test
    void shouldDeleteByToken() {
        underTest.deleteByToken(TOKEN_VALUE);
        verify(removalTokenRepository, times(1)).deleteByToken(TOKEN_VALUE);
    }
}
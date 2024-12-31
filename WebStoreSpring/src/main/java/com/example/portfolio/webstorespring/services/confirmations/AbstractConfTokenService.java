package com.example.portfolio.webstorespring.services.confirmations;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.model.entity.confirmations.BaseConfToken;
import com.example.portfolio.webstorespring.model.entity.confirmations.TokenDetails;
import com.example.portfolio.webstorespring.model.entity.subscribers.OwnerConfToken;
import com.example.portfolio.webstorespring.repositories.confirmations.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractConfTokenService<T extends BaseConfToken, S extends OwnerConfToken> {

    private final ConfirmationTokenRepository<T> tokenRepository;
    private final TokenDetailsService tokenDetailsService;

    public T getByToken(String token) {
        log.debug("Finding confirmation token by token: {}", token);
        return tokenRepository.findByTokenDetails_Token(token)
                .orElseThrow(() -> new ResourceNotFoundException("Confirmation token", "token", token));
    }

    public T createConfirmationToken(S relatedEntity, Long expiresMinute) {
        return tokenRepository.save(createTokenEntity(relatedEntity, tokenDetailsService.createTokenDetails(expiresMinute)));
    }

    @Transactional
    public Map<String, Object> confirmTokenAndExecute(String token,
                                                      Consumer<S> confirmationConsumer,
                                                      String successMessage) {
        log.info("Starting confirming token: {}.", token);
        T tokenEntity = getByToken(token);
        S relatedEntity = extractRelatedEntity(tokenEntity);
        tokenDetailsService.validateAndConfirmTokenDetails(tokenEntity.getTokenDetails());
        confirmationConsumer.accept(relatedEntity);
        log.info("Operation successful, sending message: {}", successMessage);
        return Map.of("message", successMessage);
    }

    public void delete(T tokenEntity) {
        log.debug("Deleting confirmation token for token entity: {}", tokenEntity);
        tokenRepository.delete(tokenEntity);
    }

    protected abstract T createTokenEntity(S relatedEntity, TokenDetails tokenDetails);

    protected abstract S extractRelatedEntity(T tokenEntity);


}

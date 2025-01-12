package com.example.portfolio.webstorespring.services.tokens.confirmations;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.model.entity.subscribers.OwnerConfToken;
import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.BaseConfToken;
import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.TokenDetails;
import com.example.portfolio.webstorespring.repositories.tokens.confirmations.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractConfTokenService<T extends BaseConfToken, S extends OwnerConfToken> {

    private final ConfirmationTokenRepository<T> tokenRepository;
    protected final TokenDetailsService tokenDetailsService;
    private final NotificationExpirationManager notificationExpirationManager;

    public T getByToken(String token) {
        log.info("Finding confirmation token by token: {}", token);
        return tokenRepository.findByTokenDetails_Token(token)
                .orElseThrow(() -> new ResourceNotFoundException("Confirmation token", "token", token));
    }

    @Transactional
    public T create(S relatedEntity, NotificationType notificationType) {
        log.info("Creating confirmation token for: {}", relatedEntity.getName());
        return tokenRepository.save(
                createTokenEntity(
                        relatedEntity,
                        tokenDetailsService.createTokenDetails(
                                notificationExpirationManager.getExpirationMinutes(
                                        notificationType))
                )
        );
    }

    public void delete(T tokenEntity) {
        log.debug("Deleting confirmation token for token entity: {}", tokenEntity);
        tokenRepository.delete(tokenEntity);
    }

    protected abstract T createTokenEntity(S relatedEntity, TokenDetails tokenDetails);

    public abstract S extractRelatedEntity(T tokenEntity);
}

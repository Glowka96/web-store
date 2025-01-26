package com.example.portfolio.webstorespring.services.tokens.confirmations;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.exceptions.EmailAlreadyConfirmedException;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.model.entity.subscribers.OwnerConfToken;
import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.ConfToken;
import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.TokenDetails;
import com.example.portfolio.webstorespring.repositories.tokens.confirmations.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractConfTokenService<T extends ConfToken, O extends OwnerConfToken> {

    private final ConfirmationTokenRepository<T> tokenRepository;
    protected final TokenDetailsService tokenDetailsService;
    private final NotificationExpirationManager notificationExpirationManager;

    public T getByToken(String token) {
        log.info("Finding confirmation token by token: {}", token);
        return tokenRepository.findByTokenDetails_Token(token)
                .orElseThrow(() -> new ResourceNotFoundException("Confirmation token", "token", token));
    }

    @Transactional
    public T create(O relatedEntity, NotificationType notificationType) {
        log.info("Creating confirmation token for: {}", relatedEntity.getName());
        return tokenRepository.save(
                createTokenEntity(
                        relatedEntity,
                        tokenDetailsService.create(
                                notificationExpirationManager.getExpirationMinutes(
                                        notificationType)
                        )
                )
        );
    }

    public void delete(T tokenEntity) {
        log.debug("Deleting confirmation token for token entity: {}", tokenEntity);
        tokenRepository.delete(tokenEntity);
    }

    public void validateTokenConfirmedOrOwnerEnabled(T confToken, O ownerToken) {
        log.debug("Checking if token confirmed or owner is enabled");
        if (confToken.getTokenDetails().getConfirmedAt() != null || Boolean.TRUE.equals(ownerToken.getEnabled())) {
            log.warn("Token confirmed or owner is enabled");
            throw new EmailAlreadyConfirmedException();
        }
    }

    public boolean isOwnerDisabledAndTokenExpired(O ownerToken, T confToken) {
        return Boolean.FALSE.equals(ownerToken.getEnabled()) && tokenDetailsService.isTokenExpired(confToken.getTokenDetails());
    }

    public void setConfirmedAt(T confToken) {
        tokenDetailsService.setConfirmedAt(confToken.getTokenDetails());
    }

    protected abstract T createTokenEntity(O relatedEntity, TokenDetails tokenDetails);

    public abstract O extractRelatedEntity(T tokenEntity);
}

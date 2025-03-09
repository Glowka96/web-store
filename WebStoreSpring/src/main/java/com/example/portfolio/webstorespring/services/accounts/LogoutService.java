package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.models.entities.accounts.AuthToken;
import com.example.portfolio.webstorespring.repositories.accounts.AuthTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService implements LogoutHandler {

    private final AuthTokenRepository authTokenRepository;

    @Override
    @Transactional
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        log.info("Logging out account");
        log.debug("Getting Authorization header.");
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        log.debug("Validating auth header.");
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);

        log.debug("Finding auth token by token.");
        AuthToken storedToken = authTokenRepository.findByToken(jwt)
                .orElseThrow(() -> new ResourceNotFoundException("Authorization token", "token", jwt));
        log.debug("Checking if auth token exists");
        if (storedToken != null) {
            log.debug("Setting founded auth token");
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            authTokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
            log.info("Logged out account");
        }
    }
}

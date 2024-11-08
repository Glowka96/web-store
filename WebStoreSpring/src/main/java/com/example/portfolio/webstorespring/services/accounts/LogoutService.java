package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.model.entity.accounts.AuthToken;
import com.example.portfolio.webstorespring.repositories.accounts.AuthTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final AuthTokenRepository authTokenRepository;

    @Override
    @Transactional
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        AuthToken storedToken = authTokenRepository.findByToken(jwt)
                .orElseThrow(() -> new ResourceNotFoundException("Authorization token", "token", jwt));
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            authTokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}

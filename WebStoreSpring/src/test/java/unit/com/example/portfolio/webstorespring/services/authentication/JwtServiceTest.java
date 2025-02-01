package com.example.portfolio.webstorespring.services.authentication;

import com.example.portfolio.webstorespring.configs.providers.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private JwtService underTest;

    private Key signingKey;
    private UserDetails userDetails;
    private String generatedToken;

    @BeforeEach
    void setUp() {
        String secretKey = Base64.getEncoder()
                .encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
        signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        when(jwtProvider.getSecretKey()).thenReturn(secretKey);

        userDetails = new User("user", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        generatedToken = underTest.generateToken(userDetails);
    }

    @Test
    void shouldExtractUsername() {
        String userName = underTest.extractUsername(generatedToken);

        assertEquals("user", userName);
    }

    @Test
    void shouldExtractClaim() {
        String extractedClaim = underTest.extractClaim(generatedToken, Claims::getSubject);
        assertEquals("user", extractedClaim);
    }

    @Test
    void shouldGenerateToken() {
        assertNotNull(generatedToken);
    }

    @Test
    void shouldValidateToken() {
        assertTrue(underTest.isTokenValid(generatedToken, userDetails));
    }
    @Test
    void willThrowExpiredJwtException_whenTokenIsExpired() {
        String expiredToken = Jwts.builder()
                .setClaims(Map.of("role", "ROLE_USER"))
                .setSubject("user")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        assertThrows(ExpiredJwtException.class, () -> underTest.isTokenValid(expiredToken, userDetails));
    }
}
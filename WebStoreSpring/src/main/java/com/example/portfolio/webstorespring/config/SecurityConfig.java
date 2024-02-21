package com.example.portfolio.webstorespring.config;

import com.example.portfolio.webstorespring.services.authentication.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final LogoutHandler logoutHandler;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.cors()
                .and()
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/products/**",
                                "/api/v1/logout/**",
                                "/api/v1/categories",
                                "/api/v1/products/search/**",
                                "/api/v1/subcategories/**",
                                "/api/v1/subcategories/**/products/**",
                                "/api/v1/registration/**",
                                "/api/v1/promotions/products/**",
                                "/api/v1/login",
                                "/api/v1/new-products",
                                "/api/v1/accounts/reset-password/**",
                                "/api/v1/delivery-types").permitAll()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/accounts/**",
                                "/api/v1/accounts/address/**",
                                "/api/accounts/orders",
                                "/api/v1/accounts/orders/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .headers().frameOptions().sameOrigin()
                .and()
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> {
                    logout.logoutUrl("/api/v1/logout");
                    logout.addLogoutHandler(logoutHandler);
                    logout.logoutSuccessHandler((request,
                                                 response,
                                                 authentication) -> SecurityContextHolder.clearContext());
                })
                .build();
    }
}


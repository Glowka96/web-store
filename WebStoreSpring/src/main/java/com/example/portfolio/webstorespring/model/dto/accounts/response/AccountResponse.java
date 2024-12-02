package com.example.portfolio.webstorespring.model.dto.accounts.response;

public record AccountResponse(Long id,
                              String firstName,
                              String lastName,
                              String email,
                              String imageUrl) {
}

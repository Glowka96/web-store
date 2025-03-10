package com.example.portfolio.webstorespring.models.dtos.accounts.responses;

public record AccountResponse(Long id,
                              String firstName,
                              String lastName,
                              String email,
                              String imageUrl) {
}

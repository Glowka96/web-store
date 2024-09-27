package com.example.portfolio.webstorespring.model.dto.accounts.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String imageUrl;
}

package com.example.portfolio.webstorespring.model.dto.accounts;

import com.example.portfolio.webstorespring.annotations.Password;
import lombok.Data;

@Data
public class AccountPasswordRequest {

    @Password
    private String password;
}

package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.model.dto.accounts.AccountRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.AccountResponse;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping()
    public ResponseEntity<AccountResponse> getAccount() {
        return ResponseEntity.ok(accountService.getAccount());
    }

    @PutMapping()
    public ResponseEntity<AccountResponse> updateAccount(@Valid @RequestBody AccountRequest accountRequest) {
        return ResponseEntity.accepted()
                .body(accountService.updateAccount(accountRequest));
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteAccount() {
        accountService.deleteAccount();
        return ResponseEntity.noContent().build();
    }
}

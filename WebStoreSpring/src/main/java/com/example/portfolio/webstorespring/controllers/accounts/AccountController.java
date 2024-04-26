package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountResponse;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping()
    public ResponseEntity<AccountResponse> getAccount(@AuthenticationPrincipal AccountDetails accountDetails) {
        return ResponseEntity.ok(accountService.getAccount(accountDetails));
    }

    @PutMapping()
    public ResponseEntity<AccountResponse> updateAccount(@AuthenticationPrincipal AccountDetails accountDetails,
                                                         @Valid @RequestBody AccountRequest accountRequest) {
        return ResponseEntity.accepted()
                .body(accountService.updateAccount(accountDetails ,accountRequest));
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal AccountDetails accountDetails) {
        accountService.deleteAccount(accountDetails);
        return ResponseEntity.noContent().build();
    }
}

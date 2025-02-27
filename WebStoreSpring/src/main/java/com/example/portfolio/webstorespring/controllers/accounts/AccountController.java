package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.request.UpdateEmailRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.request.UpdatePasswordRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountResponse;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public AccountResponse getAccount(@AuthenticationPrincipal AccountDetails accountDetails) {
        return accountService.getByAccountDetails(accountDetails);
    }

    @PutMapping
    public AccountResponse updateAccount(@AuthenticationPrincipal AccountDetails accountDetails,
                                         @Valid @RequestBody AccountRequest accountRequest) {
        return accountService.update(accountDetails, accountRequest);
    }

    @PatchMapping("/emails")
    public Map<String, Object> updateEmail(@AuthenticationPrincipal AccountDetails accountDetails,
                                           @RequestBody UpdateEmailRequest emailRequest) {
        return accountService.updateEmail(accountDetails, emailRequest);
    }

    @PatchMapping("/passwords")
    public Map<String, Object> updatePassword(@AuthenticationPrincipal AccountDetails accountDetails,
                                              @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        return accountService.updatePassword(accountDetails, updatePasswordRequest);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@AuthenticationPrincipal AccountDetails accountDetails) {
        accountService.delete(accountDetails);
    }
}

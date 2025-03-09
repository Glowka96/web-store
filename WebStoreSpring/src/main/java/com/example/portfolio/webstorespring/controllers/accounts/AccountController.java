package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dtos.accounts.requests.AccountRequest;
import com.example.portfolio.webstorespring.models.dtos.accounts.requests.UpdateEmailRequest;
import com.example.portfolio.webstorespring.models.dtos.accounts.requests.UpdatePasswordRequest;
import com.example.portfolio.webstorespring.models.dtos.accounts.responses.AccountResponse;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.authentications.AccountDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseMessageDTO updateEmail(@AuthenticationPrincipal AccountDetails accountDetails,
                                          @RequestBody UpdateEmailRequest request) {
        return accountService.updateEmail(accountDetails, request);
    }

    @PatchMapping("/passwords")
    public ResponseMessageDTO updatePassword(@AuthenticationPrincipal AccountDetails accountDetails,
                                             @Valid @RequestBody UpdatePasswordRequest request) {
        return accountService.updatePassword(accountDetails, request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@AuthenticationPrincipal AccountDetails accountDetails) {
        accountService.delete(accountDetails);
    }
}

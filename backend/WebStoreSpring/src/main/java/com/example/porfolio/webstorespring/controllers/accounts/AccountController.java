package com.example.porfolio.webstorespring.controllers.accounts;

import com.example.porfolio.webstorespring.model.dto.accounts.AccountDto;
import com.example.porfolio.webstorespring.services.accounts.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{accountId}")
    @PreAuthorize("@authServiceImpl.checkAuthorization(#accountId, #authHeader)")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable(value = "accountId") Long accountId,
                                                     @RequestHeader("Authorization") String authHeader ) {
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }

    @PutMapping("/{accountId}")
    @PreAuthorize("@authServiceImpl.checkAuthorization(#accountId, #authHeader)")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable(value = "accountId") Long accountId,
                                                    @Valid @RequestBody AccountDto accountDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(accountService.updateAccount(accountId, accountDto));
    }

    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@authServiceImpl.checkAuthorization(#accountId, #authHeader)")
    public void deleteAccountById(@PathVariable("accountId") Long accountId) {
        accountService.deleteAccountById(accountId);
    }
}
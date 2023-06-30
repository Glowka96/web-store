package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.model.dto.accounts.AccountAddressRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.AccountAddressResponse;
import com.example.portfolio.webstorespring.services.accounts.AccountAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/accounts/{accountId}")
@RequiredArgsConstructor
public class AccountAddressController {

    private final AccountAddressService addressService;

    @GetMapping("/address")
    @PreAuthorize("@authServiceImpl.checkAuthorization(#accountId, #authHeader)")
    public ResponseEntity<AccountAddressResponse> getAccountAddressByAccountId(@PathVariable("accountId") Long accountId,
                                                                               @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(addressService.getAccountAddressByAccountId(accountId));
    }

    @PostMapping("/address")
    @PreAuthorize("@authServiceImpl.checkAuthorization(#accountId, #authHeader)")
    public ResponseEntity<AccountAddressResponse> saveAccountAddress(@PathVariable("accountId") Long accountId,
                                                                    @Valid @RequestBody AccountAddressRequest accountAddressRequest,
                                                                    @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.saveAccountAddress(accountId, accountAddressRequest));
    }

    @PutMapping("/address")
    @PreAuthorize("@authServiceImpl.checkAuthorization(#accountId, #authHeader)")
    public ResponseEntity<AccountAddressResponse> updateAccountAddress(@PathVariable("accountId") Long accountId,
                                                                      @Valid @RequestBody AccountAddressRequest accountAddressRequest,
                                                                      @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(addressService.updateAccountAddress(accountId, accountAddressRequest));
    }
}

package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountAddressRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountAddressResponse;
import com.example.portfolio.webstorespring.services.accounts.AccountAddressService;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/accounts/addresses")
@RequiredArgsConstructor
public class AccountAddressController {

    private final AccountAddressService addressService;

    @GetMapping()
    public ResponseEntity<AccountAddressResponse> getAccountAddress(@AuthenticationPrincipal AccountDetails accountDetails) {
        return ResponseEntity.ok(addressService.getAccountAddress(accountDetails));
    }

    @PostMapping()
    public ResponseEntity<AccountAddressResponse> saveAccountAddress(@AuthenticationPrincipal AccountDetails accountDetails,
                                                                     @Valid @RequestBody AccountAddressRequest accountAddressRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.saveAccountAddress(accountDetails, accountAddressRequest));
    }

    @PutMapping()
    public ResponseEntity<AccountAddressResponse> updateAccountAddress(@AuthenticationPrincipal AccountDetails accountDetails,
                                                                       @Valid @RequestBody AccountAddressRequest accountAddressRequest) {
        return ResponseEntity.accepted()
                .body(addressService.updateAccountAddress(accountDetails, accountAddressRequest));
    }
}

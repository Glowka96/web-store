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
@RequestMapping(value = "api/v1/accounts/address")
@RequiredArgsConstructor
public class AccountAddressController {

    private final AccountAddressService addressService;

    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    @GetMapping()
    public ResponseEntity<AccountAddressResponse> getAccountAddress() {
        return ResponseEntity.ok(addressService.getAccountAddress());
    }

    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    @PostMapping()
    public ResponseEntity<AccountAddressResponse> saveAccountAddress(@Valid @RequestBody AccountAddressRequest accountAddressRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.saveAccountAddress(accountAddressRequest));
    }
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    @PutMapping()
    public ResponseEntity<AccountAddressResponse> updateAccountAddress(@Valid @RequestBody AccountAddressRequest accountAddressRequest) {
        return ResponseEntity.accepted()
                .body(addressService.updateAccountAddress(accountAddressRequest));
    }
}

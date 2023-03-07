package com.example.porfolio.webstorespring.controllers;

import com.example.porfolio.webstorespring.model.dto.accounts.AccountAddressDto;
import com.example.porfolio.webstorespring.services.AccountAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("api/v1/accounts/{" +
        "accountId}")
@RequiredArgsConstructor
public class AccountAddressController {

    private final AccountAddressService addressService;

    @GetMapping("/address")
    public ResponseEntity<AccountAddressDto> getAccountAddressByAccountId(@PathVariable("accountId") Long accountId) {
        return ResponseEntity.ok(addressService.getAccountAddressByAccountId(accountId));
    }

    @PostMapping("/address")
    public ResponseEntity<AccountAddressDto> saveAccountAddress(@PathVariable("accountId") Long accountId,
                                                                @Valid @RequestBody AccountAddressDto accountAddressDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.saveAccountAddress(accountId, accountAddressDto));
    }

    @PutMapping("/address")
    public ResponseEntity<AccountAddressDto> updateAccountAddress(@PathVariable("accountId") Long accountId,
                                                                  @Valid @RequestBody AccountAddressDto accountAddressDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(addressService.updateAccountAddress(accountId, accountAddressDto));
    }
}

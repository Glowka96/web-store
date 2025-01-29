package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountAddressRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountAddressResponse;
import com.example.portfolio.webstorespring.services.accounts.AccountAddressService;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/accounts/addresses")
@RequiredArgsConstructor
public class AccountAddressController {

    private final AccountAddressService addressService;

    @GetMapping
    public AccountAddressResponse getAccountAddress(@AuthenticationPrincipal AccountDetails accountDetails) {
        return addressService.getByAccountDetails(accountDetails);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountAddressResponse saveAccountAddress(@AuthenticationPrincipal AccountDetails accountDetails,
                                                     @Valid @RequestBody AccountAddressRequest request) {
        return addressService.save(accountDetails, request);
    }

    @PutMapping
    public AccountAddressResponse updateAccountAddress(@AuthenticationPrincipal AccountDetails accountDetails,
                                                       @Valid @RequestBody AccountAddressRequest request) {
        return addressService.update(accountDetails, request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccountAddress(@AuthenticationPrincipal AccountDetails accountDetails) {
        addressService.deleteByAccountDetails(accountDetails);
    }
}

package com.example.porfolio.webstorespring.model.dto.accounts;

import com.example.porfolio.webstorespring.model.dto.accounts.annotations.Password;
import com.example.porfolio.webstorespring.model.dto.orders.OrderDto;
import com.example.porfolio.webstorespring.model.entity.accounts.AccountRoles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;

import java.util.List;

public record AccountDto(
        @JsonIgnore
        Long id,
        String firstName,
        String lastName,
        @Email(message = "login should be a valid e-mail address format")
        String email,
        @Password
        String password,
        AccountAddressDto address,
        String imageUrl,
        AccountRoles accountRoles,
        List<OrderDto> ordersDto
) {

}

package com.example.porfolio.webstorespring.model.dto.accounts;

import com.example.porfolio.webstorespring.annotations.Password;
import com.example.porfolio.webstorespring.annotations.UniqueEmail;
import com.example.porfolio.webstorespring.model.dto.orders.OrderDto;
import com.example.porfolio.webstorespring.model.entity.accounts.AccountRoles;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.util.List;

@Data
public class AccountDto {

    private Long id;

    private String firstName;

    private String lastName;

    @Email(message = "Login should be a valid e-mail address format")
    @UniqueEmail()
    private String email;

    @Password
    private String password;

    private AccountAddressDto address;

    private String imageUrl;

    private Boolean enabled;

    private AccountRoles accountRoles;

    private List<OrderDto> ordersDto;
}

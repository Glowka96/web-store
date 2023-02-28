package com.example.porfolio.webstorespring.model.dto.accounts;

import com.example.porfolio.webstorespring.model.dto.accounts.annotations.Password;
import com.example.porfolio.webstorespring.model.dto.orders.OrderDto;
import com.example.porfolio.webstorespring.model.entity.accounts.AccountRoles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Data
public class AccountDto {

    @JsonIgnore
    private Long id;

    private String firstName;

    private String lastName;

    @Email(message = "login should be a valid e-mail address format")
    @UniqueElements(message = "The email is already in use")
    private String email;

    @Password
    private String password;

    private AccountAddressDto address;

    private String imageUrl;

    private Boolean enabled;

    private AccountRoles accountRoles;

    private List<OrderDto> ordersDto;
}

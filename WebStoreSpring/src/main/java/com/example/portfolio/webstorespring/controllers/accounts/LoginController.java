package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.models.dto.accounts.request.LoginRequest;
import com.example.portfolio.webstorespring.models.dto.accounts.response.AuthenticationResponse;
import com.example.portfolio.webstorespring.services.accounts.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public AuthenticationResponse login(@RequestBody LoginRequest request) {
        return loginService.login(request);
    }
}

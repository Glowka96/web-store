package com.example.porfolio.webstorespring.controllers.accounts;

import com.example.porfolio.webstorespring.model.dto.accounts.LoginRequest;
import com.example.porfolio.webstorespring.services.accounts.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/v1/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping()
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
//        HttpHeaders headers = new HttpHeaders();
//        AuthenticationResponse authenticationResponse = loginService.login(loginRequest);
//        String jwtToken = authenticationResponse.getToken();
//        String bearer = "Bearer " + jwtToken;
//        headers.set("Authorization", bearer);c
        return ResponseEntity.ok().body(loginService.login(loginRequest));
    }
}

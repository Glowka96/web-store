package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.annotations.Password;
import com.example.portfolio.webstorespring.services.accounts.ResetPasswordService;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "api/v1/accounts/reset-password")
@RequiredArgsConstructor
@Validated
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;
    @GetMapping(params = "email")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestParam("email") @Email(message = "Invalid email format") String email) {
        return ResponseEntity.ok(resetPasswordService.resetPasswordByEmail(email));
    }

    @PatchMapping(value = "confirm", params = {"password", "token"})
    public ResponseEntity<Map<String, Object>> confirmResetPassword(@RequestParam("password") @Password String password,
                                                                    @RequestParam("token") String token) {
        return ResponseEntity.accepted()
                .body(resetPasswordService.confirmResetPassword(password, token));
    }
}

package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.model.dto.accounts.request.ResetPasswordRequest;
import com.example.portfolio.webstorespring.services.accounts.ResetPasswordService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "api/v1/reset-password")
@RequiredArgsConstructor
@Validated
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;
    @GetMapping(params = "email")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestParam("email") @Email(message = "Invalid email format") String email) {
        return ResponseEntity.ok(resetPasswordService.resetPasswordByEmail(email));
    }

    @PatchMapping(value = "/confirm", params = {"token"})
    public ResponseEntity<Map<String, Object>> confirmResetPassword(@RequestBody() @Valid ResetPasswordRequest resetPasswordRequest,
                                                                    @RequestParam("token") String token) {
        return ResponseEntity.accepted()
                .body(resetPasswordService.confirmResetPassword(resetPasswordRequest, token));
    }
}

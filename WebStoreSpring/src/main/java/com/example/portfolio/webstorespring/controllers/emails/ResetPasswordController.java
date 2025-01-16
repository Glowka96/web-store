package com.example.portfolio.webstorespring.controllers.emails;

import com.example.portfolio.webstorespring.model.dto.accounts.request.ResetPasswordRequest;
import com.example.portfolio.webstorespring.services.email.ResetPasswordService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping( "api/v1/reset-password")
@RequiredArgsConstructor
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    @GetMapping(params = "email")
    public Map<String, Object> resetPassword(@RequestParam("email") @Email(message = "Invalid email format") String email) {
        return resetPasswordService.sendResetPasswordLinkByEmail(email);
    }

    @PatchMapping(value = "/confirm", params = {"token"})
    public Map<String, Object> confirmResetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest,
                                                    @RequestParam("token") String token) {
        return resetPasswordService.confirm(resetPasswordRequest, token);
    }
}

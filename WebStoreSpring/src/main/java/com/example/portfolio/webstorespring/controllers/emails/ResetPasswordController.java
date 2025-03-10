package com.example.portfolio.webstorespring.controllers.emails;

import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dtos.accounts.requests.ResetPasswordRequest;
import com.example.portfolio.webstorespring.services.emails.accountactions.ResetPasswordService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "api/v1/reset-passwords")
@RequiredArgsConstructor
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    @GetMapping(params = "email")
    public ResponseMessageDTO sendResetPasswordLink(@RequestParam("email") @Email(message = "Invalid email format") String email) {
        return resetPasswordService.sendResetPasswordLinkByEmail(email);
    }

    @PatchMapping(value = "/confirm", params = {"token"})
    public ResponseMessageDTO confirmResetPassword(@RequestBody @Valid ResetPasswordRequest request,
                                                    @RequestParam("token") String token) {
        return resetPasswordService.confirm(request, token);
    }
}

package com.example.portfolio.webstorespring.controllers.emails;

import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.accounts.request.ResetPasswordRequest;
import com.example.portfolio.webstorespring.services.emails.ResetPasswordService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "api/v1/reset-password")
@RequiredArgsConstructor
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    @GetMapping(params = "email")
    public ResponseMessageDTO resetPassword(@RequestParam("email") @Email(message = "Invalid email format") String email) {
        return resetPasswordService.sendResetPasswordLinkByEmail(email);
    }

    @PatchMapping(value = "/confirm", params = {"token"})
    public ResponseMessageDTO confirmResetPassword(@RequestBody @Valid ResetPasswordRequest request,
                                                    @RequestParam("token") String token) {
        return resetPasswordService.confirm(request, token);
    }
}

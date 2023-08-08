package com.example.portfolio.webstorespring.controllers.accounts;

import com.example.portfolio.webstorespring.model.dto.accounts.AccountPasswordRequest;
import com.example.portfolio.webstorespring.services.accounts.ResetPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;
    @GetMapping(value = "/{email}/reset-assword")
    public ResponseEntity<Map<String, Object>> resetPassword(@PathVariable("email") String email) {
        return ResponseEntity.ok(resetPasswordService.resetPassword(email));
    }

    @PatchMapping(value = "/confirm", params = "token")
    public ResponseEntity<Map<String, Object>> confirmResetPassword(@Valid @RequestBody AccountPasswordRequest accountPasswordRequest,
                                                                    @RequestParam("token") String token) {
        return ResponseEntity.ok(resetPasswordService.confirmResetPassword(accountPasswordRequest.getPassword(), token));
    }
}

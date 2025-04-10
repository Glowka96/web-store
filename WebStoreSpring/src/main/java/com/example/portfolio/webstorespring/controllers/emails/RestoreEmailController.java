package com.example.portfolio.webstorespring.controllers.emails;

import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.services.emails.accountactions.RestoreEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/restore-emails/confirm")
@RequiredArgsConstructor
public class RestoreEmailController {

    private final RestoreEmailService restoreEmailService;

    @PatchMapping(params = {"token"})
    public ResponseMessageDTO confirmRestoreEmail(@RequestParam("token") String token) {
        return restoreEmailService.confirm(token);
    }
}

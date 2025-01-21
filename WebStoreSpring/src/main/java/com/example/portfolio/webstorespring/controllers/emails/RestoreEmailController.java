package com.example.portfolio.webstorespring.controllers.emails;

import com.example.portfolio.webstorespring.services.emails.RestoreEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/restore-email/confirm")
@RequiredArgsConstructor
public class RestoreEmailController {

    private final RestoreEmailService restoreEmailService;

    @PatchMapping(params = {"token"})
    public Map<String, Object> confirmRestoreEmail(@RequestParam("token") String token) {
        return restoreEmailService.confirm(token);
    }
}

package org.aper.web.domain.chat.controller;

import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/health")
    public String checkHealth() {
        return "OK";
    }

    @GetMapping("/health/auth")
    public String authHealthCheck(@AuthenticationPrincipal UserDetailsImpl userDetails) { return userDetails.getUsername(); }

}

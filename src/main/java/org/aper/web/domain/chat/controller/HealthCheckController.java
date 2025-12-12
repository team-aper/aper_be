package org.aper.web.domain.chat.controller;

import com.aper.submodule.gateway.annotation.CurrentUser;
import com.aper.submodule.gateway.dto.UserInfo;
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

    @GetMapping("/health/ver2")
    public String authHealthCheck2(@CurrentUser UserInfo userInfo) { return userInfo.getEmail(); }
}

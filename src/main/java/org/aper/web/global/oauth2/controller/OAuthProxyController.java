package org.aper.web.global.oauth2.controller;

import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2/authorization")
public class OAuthProxyController {

    @GetMapping("/{provider}")
    public ResponseEntity<Void> redirectToProvider(@PathVariable String provider) {
        String authorizationUrl = getAuthorizationUrl(provider); // OAuth 인증 URL 생성
        return ResponseEntity.status(HttpStatus.FOUND) // 리다이렉션 응답
                             .header(HttpHeaders.LOCATION, authorizationUrl)
                             .build();
    }

    private String getAuthorizationUrl(String provider) {
        switch (provider) {
            case "google":
                return "https://accounts.google.com/o/oauth2/v2/auth?..."; // Google 인증 URL
            case "kakao":
                return "https://kauth.kakao.com/oauth/authorize?...";     // Kakao 인증 URL
            case "naver":
                return "https://nid.naver.com/oauth2.0/authorize?...";    // Naver 인증 URL
            default:
                throw new ServiceException(ErrorCode.INVALID_PROVIDER_REQUEST, provider);
        }
    }
}

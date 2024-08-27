package org.aper.web.global.jwt.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.aper.web.domain.user.dto.UserRequestDto.LoginRequestDto;
import org.aper.web.global.docs.AuthControllerDocs;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.jwt.dto.GeneratedToken;
import org.aper.web.global.jwt.dto.UserInfo;
import org.aper.web.global.jwt.service.AuthService;
import org.aper.web.global.jwt.service.CookieService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;
    private final CookieService cookieService;

    public AuthController(AuthService authService, CookieService cookieService) {
        this.authService = authService;
        this.cookieService = cookieService;
    }

    @PostMapping("/login")
    public ResponseDto<UserInfo> login(@RequestBody @Valid LoginRequestDto loginRequestDto, HttpServletResponse response) throws UnsupportedEncodingException {
        GeneratedToken tokens = authService.authenticateAndLogin(loginRequestDto, response);
        setTokensInResponse(tokens, response);
        UserInfo userInfo = authService.getUserInfo(loginRequestDto);
        return ResponseDto.success("로그인 성공", userInfo);
    }

    @PostMapping("/reissue")
    public ResponseDto<Void> reissue(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        GeneratedToken tokens = authService.reissue(request, response);
        setTokensInResponse(tokens, response);
        return ResponseDto.success("토큰 재발급 성공");
    }

    private void setTokensInResponse(GeneratedToken tokens, HttpServletResponse response) throws UnsupportedEncodingException {
        response.setHeader("Authorization", tokens.getAccessToken());
        cookieService.setCookie(response, "Refresh-Token", tokens.getRefreshToken());
    }
}

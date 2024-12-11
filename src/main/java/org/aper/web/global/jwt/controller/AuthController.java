package org.aper.web.global.jwt.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aper.web.domain.user.dto.UserRequestDto.LoginRequestDto;
import org.aper.web.global.docs.AuthControllerDocs;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.jwt.dto.AuthRequestDto;
import org.aper.web.global.jwt.dto.AuthResponseDto.UserInfo;
import org.aper.web.global.jwt.dto.token.GeneratedToken;
import org.aper.web.global.jwt.service.AuthService;
import org.aper.web.global.jwt.service.CookieService;
import org.aper.web.global.jwt.service.LogoutService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;
    private final CookieService cookieService;
    private final LogoutService logoutService;

    @PostMapping("/login")
    public ResponseDto<UserInfo> login(@RequestBody @Valid LoginRequestDto loginRequestDto, HttpServletResponse response) {
        GeneratedToken tokens = authService.authenticateAndLogin(loginRequestDto);
        setTokensInResponse(tokens, response);
        UserInfo userInfo = authService.getUserInfo(loginRequestDto.email());
        return ResponseDto.success("로그인 성공", userInfo);
    }

    @PostMapping("/reissue")
    public ResponseDto<Void> reissue(HttpServletRequest request, HttpServletResponse response) {
        GeneratedToken tokens = authService.reissue(request, response);
        setTokensInResponse(tokens, response);
        return ResponseDto.success("토큰 재발급 성공");
    }

    @PostMapping("/oauth/login")
    public ResponseDto<UserInfo> oauthLogin(@RequestBody AuthRequestDto.GetMeRequestDto getMeRequestDto, HttpServletResponse response) {
        UserInfo userInfo = authService.oauthLogin(getMeRequestDto, response);
        return ResponseDto.success("User Info Data", userInfo);
    }

    @PostMapping("/logout")
    public ResponseDto<Void> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutService.logout(request, response, authentication);
        SecurityContextHolder.clearContext();
        return ResponseDto.success("로그아웃 성공");
    }

    private void setTokensInResponse(GeneratedToken tokens, HttpServletResponse response) {
        response.setHeader("Authorization", tokens.getAccessToken());
        cookieService.setCookie(response, "Refresh-Token", tokens.getRefreshToken());
    }
}

package org.aper.web.global.jwt.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aper.web.global.handler.CustomResponseUtil;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.jwt.TokenProvider;
import org.aper.web.global.jwt.service.token.RefreshTokenService;
import org.aper.web.global.jwt.service.token.TokenValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final TokenValidationService tokenValidationService;
    private final CookieService cookieService;

    public LogoutService(TokenProvider tokenProvider, RefreshTokenService refreshTokenService, TokenValidationService tokenValidationService, CookieService cookieService) {
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.tokenValidationService = tokenValidationService;
        this.cookieService = cookieService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String accessTokenValue = tokenProvider.getJwtFromHeader(request);

        if (accessTokenValue.isEmpty()) {
            CustomResponseUtil.fail(response, ErrorCode.ACCESS_TOKEN_IS_NULL);
        }

        String email = tokenProvider.getUserInfoFromAccessToken(accessTokenValue).getSubject();
        String storedRefreshToken = refreshTokenService.getRefreshToken(email);
        tokenValidationService.verifyRefreshToken(storedRefreshToken);

        refreshTokenService.deleteRefreshToken(email);
        cookieService.deleteCookie(request, response, "Refresh-Token");

        CustomResponseUtil.success(response, "로그아웃 성공", HttpStatus.OK);
    }
}

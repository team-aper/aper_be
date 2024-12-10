package org.aper.web.global.handler.authHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.jwt.TokenProvider;
import org.aper.web.global.jwt.dto.GeneratedToken;
import org.aper.web.global.jwt.service.CookieService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final CookieService cookieService;

    @Override
    @Transactional(readOnly = true)
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {

        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");
            String role = oAuth2User.getAuthorities().stream()
                    .findFirst()
                    .orElseThrow(() -> new ServiceException(ErrorCode.AUTH_NOT_FOUND))
                    .getAuthority();

            boolean isExist = Boolean.TRUE.equals(oAuth2User.getAttribute("exist"));

            if (!isExist) {
                throw new ServiceException(ErrorCode.OAUTH2_USER_NOT_FOUND);
            }

            // JWT 토큰 생성
            GeneratedToken tokens = tokenProvider.generateToken(email, role, name);
            response.setHeader("Authorization", tokens.getAccessToken());
            cookieService.setCookie(response, "Refresh-Token", tokens.getRefreshToken());

            // 원하는 리다이렉션 경로
            String redirectUrl = "https://www.aper.cc/oauth/success";
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            log.error("Error during authentication success handling", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

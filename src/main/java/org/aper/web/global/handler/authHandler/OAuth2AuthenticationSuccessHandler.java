package org.aper.web.global.handler.authHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.jwt.service.token.TempTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TempTokenService tempTokenService; // 임시 토큰 저장소

    @Override
    @Transactional(readOnly = true)
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {

        try {
            response.setHeader("Access-Control-Allow-Origin", "https://www.aper.cc");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");

            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");

            boolean isExist = Boolean.TRUE.equals(oAuth2User.getAttribute("exist"));
            if (!isExist) {
                throw new ServiceException(ErrorCode.OAUTH2_USER_NOT_FOUND);
            }

            String tempToken = tempTokenService.generateTempToken(email);

            String redirectUrl = "https://www.aper.cc/oauth/success?tempToken=" + tempToken;
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            log.error("Error during authentication success handling", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
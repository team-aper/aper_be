package org.aper.web.global.handler.authHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.global.handler.CustomResponseUtil;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.jwt.TokenProvider;
import org.aper.web.global.jwt.dto.GeneratedToken;
import org.aper.web.global.jwt.service.CookieService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final CookieService cookieService;

    public OAuth2AuthenticationSuccessHandler(TokenProvider tokenProvider, CookieService cookieService) {
        this.tokenProvider = tokenProvider;
        this.cookieService = cookieService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String provider = (String) request.getAttribute("provider"); // 예: kakao, google


        String role = oAuth2User.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new ServiceException(ErrorCode.AUTH_NOT_FOUND))
                .getAuthority();

        boolean isExist = Boolean.TRUE.equals(oAuth2User.getAttribute("exist"));

        if (!isExist) {
            log.error("회원이 존재하지 않습니다: {}", email);

            Map<String, String> errors = new HashMap<>();
            errors.put("errorCode", ErrorCode.USER_NOT_FOUND.name());
            errors.put("cause", "OAuth2 인증 중 사용자를 찾을 수 없습니다.");
            errors.put("provider", provider);

            CustomResponseUtil.fail(response, ErrorCode.USER_NOT_FOUND);
            return;
        }

        GeneratedToken tokens = tokenProvider.generateToken(email, role, name);
        cookieService.setCookie(response, "Refresh-Token", tokens.getRefreshToken());

        Map<String, Object> data = new HashMap<>();
        data.put("accessToken", tokens.getAccessToken());
        data.put("refreshToken", tokens.getRefreshToken());
        data.put("provider", provider);

        CustomResponseUtil.success(response, "OAuth2 인증이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
}

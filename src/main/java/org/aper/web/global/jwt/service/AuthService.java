package org.aper.web.global.jwt.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.user.dto.UserRequestDto.LoginRequestDto;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.service.UserService;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.handler.exception.TokenException;
import org.aper.web.global.jwt.TokenProvider;
import org.aper.web.global.jwt.dto.GeneratedToken;
import org.aper.web.global.jwt.dto.UserInfo;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;

@Service
@Slf4j
public class AuthService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenBlacklistService tokenBlacklistService;
    private final RefreshTokenService refreshTokenService;
    private final CookieService cookieService;
    private final TokenValidationService tokenValidationService;

    public AuthService(TokenProvider tokenProvider,
                       AuthenticationManager authenticationManager,
                       UserService userService,
                       TokenBlacklistService tokenBlacklistService,
                       RefreshTokenService refreshTokenService,
                       CookieService cookieService,
                       TokenValidationService tokenValidationService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.refreshTokenService = refreshTokenService;
        this.cookieService = cookieService;
        this.tokenValidationService = tokenValidationService;
    }

    public GeneratedToken authenticateAndLogin(@Valid LoginRequestDto requestDto, HttpServletResponse response) {
        try {
            Authentication authentication = authenticateUser(requestDto.email(), requestDto.password());
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            String penName = userDetails.user().getPenName();

            return tokenProvider.generateToken(userDetails.getUsername(), role, penName);
        } catch (AuthenticationException e) {
            throw new ServiceException(ErrorCode.AUTHENTICATION_FAILED);
        }
    }

    private Authentication authenticateUser(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    @Transactional
    public GeneratedToken reissue(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String accessTokenValue = tokenProvider.getJwtFromHeader(request);
        validateTokenPresence(accessTokenValue);

        Claims claims = tokenValidationService.verifyAccessToken(accessTokenValue);
        String email = claims.getSubject();

        String storedRefreshToken = refreshTokenService.getRefreshToken(email);
        tokenValidationService.verifyRefreshToken(storedRefreshToken);

        // 블랙리스트 추가
        blacklistTokens(accessTokenValue, storedRefreshToken);

        return generateNewTokens(email, request, response);
    }

    private void validateTokenPresence(String token) {
        if (!StringUtils.hasText(token)) {
            throw new TokenException(HttpStatus.BAD_REQUEST, ErrorCode.ACCESS_TOKEN_IS_NULL);
        }
    }

    private void blacklistTokens(String accessToken, String refreshToken) {
        tokenBlacklistService.saveBlackListToken(accessToken);
        tokenBlacklistService.saveBlackListToken(refreshToken);
    }

    private GeneratedToken generateNewTokens(String email, HttpServletRequest request, HttpServletResponse response) {
        refreshTokenService.deleteRefreshToken(email);
        cookieService.deleteCookie(request, response, "Refresh-Token");

        User user = userService.findUser(email);
        return tokenProvider.generateToken(user.getEmail(), user.getRole().getAuthority(), user.getPenName());
    }

    @Transactional(readOnly = true)
    public UserInfo getUserInfo(LoginRequestDto loginRequestDto) {
        User user = userService.findUser(loginRequestDto.email());
        return new UserInfo(user.getEmail(), user.getPenName(), user.getFieldImage());
    }
}

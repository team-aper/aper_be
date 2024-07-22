package org.aper.web.global.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.user.dto.UserRequestDto.LoginRequestDto;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.service.UserService;
import org.aper.web.global.handler.CustomResponseUtil;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.handler.exception.TokenException;
import org.aper.web.global.jwt.TokenProvider;
import org.aper.web.global.jwt.dto.GeneratedToken;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        if (accessTokenValue.isEmpty()) {
            CustomResponseUtil.fail(response, ErrorCode.ACCESS_TOKEN_IS_NULL.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (tokenBlacklistService.isTokenBlacklisted(accessTokenValue)) {
            log.error("blacklisted Token");
            throw new TokenException(HttpStatus.FORBIDDEN, ErrorCode.BLACK_LISTED_TOKEN);
        }

        tokenBlacklistService.saveBlackListToken(accessTokenValue);

        Claims claims = null;

        try {
            claims = tokenProvider.getUserInfoFromAccessToken(accessTokenValue);
        } catch (TokenException e) {
            CustomResponseUtil.fail(response, e.getMessage(), HttpStatus.FORBIDDEN);
        }

        if (claims == null) {
            throw new TokenException(HttpStatus.FORBIDDEN, ErrorCode.INVALID_ACCESS_TOKEN);
        }

        String email = claims.getSubject();
        String storedRefreshToken = refreshTokenService.getRefreshToken(email);
        tokenValidationService.verifyRefreshToken(storedRefreshToken);

        refreshTokenService.deleteRefreshToken(email);
        cookieService.deleteCookie(request, response, "Refresh-Token");

        User user = userService.findUser(email);
        return tokenProvider.generateToken(user.getEmail(), user.getRole().getAuthority(), user.getPenName());
    }

}

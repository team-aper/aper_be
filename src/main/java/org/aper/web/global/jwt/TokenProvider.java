package org.aper.web.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.global.jwt.dto.GeneratedToken;
import org.aper.web.global.jwt.service.RefreshTokenService;
import org.aper.web.global.jwt.service.TokenValidationService;
import org.aper.web.global.properties.JwtProperties;
import org.aper.web.global.properties.TokenProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {

    private final TokenProperties tokenProperties;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final RefreshTokenService tokenService;
    private final Key accessKey;
    private final Key refreshKey;
    private final TokenValidationService tokenValidationService;
    private static final String BEARER_PREFIX = "Bearer ";

    public TokenProvider(RefreshTokenService tokenService, JwtProperties jwtProperties, TokenValidationService tokenValidationService, TokenProperties tokenProperties) {
        this.tokenService = tokenService;
        this.accessKey = jwtProperties.accessKey();
        this.refreshKey = jwtProperties.refreshKey();
        this.tokenValidationService = tokenValidationService;
        this.tokenProperties = tokenProperties;
    }

    public GeneratedToken generateToken(String email, String role, String penName) {
        String accessToken = generateAccessToken(email, role, penName);
        String refreshToken = generateRefreshToken(email, role, penName);

        tokenService.saveTokenInfo(email, refreshToken, tokenProperties.getRefreshTokenExpiration());
        return new GeneratedToken(accessToken, refreshToken);
    }

    public String generateAccessToken(String email, String role, String penName) {
        return BEARER_PREFIX + generateToken(email, role, penName, tokenProperties.getAccessTokenExpiration(), accessKey);
    }

    public String generateRefreshToken(String email, String role, String penName) {
        return generateToken(email, role, penName, tokenProperties.getRefreshTokenExpiration(), refreshKey);
    }

    private String generateToken(String email, String role, String penName, long expirationTime, Key key) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(email)
                .claim(tokenProperties.getAuthorizationKey(), role)
                .claim("penName", penName)
                .setExpiration(new Date(now.getTime() + expirationTime))
                .setIssuedAt(now)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public Claims getUserInfoFromAccessToken(String accessToken) {
        return tokenValidationService.verifyAccessToken(accessToken);
    }

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(tokenProperties.getAuthorizationHeader());
        return removeBearerPrefix(bearerToken);
    }

    public String removeBearerPrefix(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

}

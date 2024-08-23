package org.aper.web.global.jwt.service;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.TokenException;
import org.aper.web.global.jwt.dto.TokenVerificationResult;
import org.aper.web.global.jwt.dto.VerifiedToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Key;

@Slf4j
@Service
public class TokenValidationService {

    private final Key accessKey;
    private final Key refreshKey;
    private final TokenBlacklistService tokenBlacklistService;

    public TokenValidationService(Key accessKey, Key refreshKey, TokenBlacklistService tokenBlacklistService) {
        this.accessKey = accessKey;
        this.refreshKey = refreshKey;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    public Claims verifyAccessToken(String accessToken) {
        // 블랙리스트 검증
        checkIfBlacklisted(accessToken);

        // 토큰 유효성 검증
        VerifiedToken verificationResult = verifyToken(accessToken, accessKey);
        if (verificationResult.getTokenVerificationResult().equals(TokenVerificationResult.EXPIRED)) {
            return verificationResult.getClaims();  // 만료된 경우에도 클레임을 반환
        }

        if (verificationResult.getTokenVerificationResult().equals(TokenVerificationResult.INVALID)) {
            throw new TokenException(HttpStatus.FORBIDDEN, ErrorCode.INVALID_ACCESS_TOKEN);
        }

        if (verificationResult.getTokenVerificationResult().equals(TokenVerificationResult.NULL)) {
            throw new TokenException(HttpStatus.UNAUTHORIZED, ErrorCode.ACCESS_TOKEN_IS_NULL);
        }

        return verificationResult.getClaims();
    }

    public void verifyRefreshToken(String refreshToken) {
        // 블랙리스트 검증
        checkIfBlacklisted(refreshToken);

        // 리프레시 토큰 유효성 검증
        VerifiedToken verificationResult = verifyToken(refreshToken, refreshKey);
        if (verificationResult.getTokenVerificationResult().equals(TokenVerificationResult.EXPIRED)) {
            throw new TokenException(HttpStatus.FORBIDDEN, ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        if (verificationResult.getTokenVerificationResult().equals(TokenVerificationResult.INVALID)) {
            throw new TokenException(HttpStatus.FORBIDDEN, ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (verificationResult.getTokenVerificationResult().equals(TokenVerificationResult.NULL)) {
            throw new TokenException(HttpStatus.FORBIDDEN, ErrorCode.REFRESH_TOKEN_NOT_EXISTS);
        }
    }

    private void checkIfBlacklisted(String token) {
        if (tokenBlacklistService.isTokenBlacklisted(token)) {
            throw new TokenException(HttpStatus.FORBIDDEN, ErrorCode.BLACK_LISTED_TOKEN);
        }
    }

    private VerifiedToken verifyToken(String token, Key key) {
        Claims claims = null;

        try {
            claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
            return VerifiedToken.builder().tokenVerificationResult(TokenVerificationResult.EXPIRED).claims(e.getClaims()).build();
        } catch (SecurityException | MalformedJwtException | io.jsonwebtoken.security.SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            return VerifiedToken.builder().tokenVerificationResult(TokenVerificationResult.INVALID).build();
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
            return VerifiedToken.builder().tokenVerificationResult(TokenVerificationResult.INVALID).build();
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            return VerifiedToken.builder().tokenVerificationResult(TokenVerificationResult.NULL).build();
        }
        return VerifiedToken.builder().tokenVerificationResult(TokenVerificationResult.VALID).claims(claims).build();
    }
}

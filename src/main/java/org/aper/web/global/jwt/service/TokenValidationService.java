package org.aper.web.global.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.TokenException;
import org.aper.web.global.jwt.dto.TokenVerificationResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Slf4j
@Service
public class TokenValidationService {

    private final Key accessKey;
    private final Key refreshKey;

    public TokenValidationService(Key accessKey, Key refreshKey) {
        this.accessKey = accessKey;
        this.refreshKey = refreshKey;
    }

    public void verifyAccessToken(String accessToken) {
        TokenVerificationResult verificationResult = verifyToken(accessToken, accessKey);

        if (verificationResult.equals(TokenVerificationResult.EXPIRED)){
            throw new TokenException(HttpStatus.UNAUTHORIZED, ErrorCode.EXPIRED_ACCESS_TOKEN);
        }

        if (verificationResult.equals(TokenVerificationResult.INVALID)){
            throw new TokenException(HttpStatus.FORBIDDEN, ErrorCode.INVALID_ACCESS_TOKEN);
        }

        if (verificationResult.equals(TokenVerificationResult.NULL)){
            throw new TokenException(HttpStatus.FORBIDDEN, ErrorCode.ACCESS_TOKEN_IS_NULL);
        }
    }

    public void verifyRefreshToken(String refreshToken) {
        Object verificationResult = verifyToken(refreshToken, refreshKey);

        if (verificationResult.equals(TokenVerificationResult.EXPIRED)){
            throw new TokenException(HttpStatus.FORBIDDEN, ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        if (verificationResult.equals(TokenVerificationResult.INVALID)){
            throw new TokenException(HttpStatus.FORBIDDEN, ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (verificationResult.equals(TokenVerificationResult.NULL)){
            throw new TokenException(HttpStatus.FORBIDDEN, ErrorCode.REFRESH_TOKEN_NOT_EXISTS);
        }
    }

    public TokenVerificationResult verifyToken(String token, Key key) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            if (isExpired(claims)) {
                log.error("Expired JWT token, 만료된 JWT AccessToken 입니다.");
                return TokenVerificationResult.EXPIRED;
            }
        } catch (SecurityException | MalformedJwtException | io.jsonwebtoken.security.SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            return TokenVerificationResult.INVALID;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
            return TokenVerificationResult.INVALID;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            return TokenVerificationResult.NULL;
        }
        return TokenVerificationResult.VALID;
    }

    private boolean isExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}

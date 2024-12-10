package org.aper.web.global.jwt.dto.token;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VerifiedToken {
    private TokenVerificationResult tokenVerificationResult;
    private Claims claims;

}

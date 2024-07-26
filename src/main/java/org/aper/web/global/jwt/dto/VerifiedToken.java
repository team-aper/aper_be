package org.aper.web.global.jwt.dto;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VerifiedToken {
    private TokenVerificationResult tokenVerificationResult;
    private Claims claims;

}

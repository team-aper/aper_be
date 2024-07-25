package org.aper.web.global.handler.exception;

import lombok.Getter;
import org.aper.web.global.handler.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Getter
public class TokenException extends AuthenticationException {

    private final ErrorCode errorCode;
    private final HttpStatus status;

    public TokenException(HttpStatus status, ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.status = status;
    }

}

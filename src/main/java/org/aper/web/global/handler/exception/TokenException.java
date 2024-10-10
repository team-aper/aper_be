package org.aper.web.global.handler.exception;

import lombok.Getter;
import org.aper.web.global.handler.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Getter
public class TokenException extends AuthenticationException {

    private final HttpStatus status;
    private final String code;

    public TokenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

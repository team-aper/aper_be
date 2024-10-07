package org.aper.web.global.handler.exception;

import lombok.Getter;
import org.aper.web.global.dto.ErrorResponseDto;
import org.aper.web.global.handler.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends RuntimeException {

    private final HttpStatus status;
    private final String code;
    private final String message;

    public ServiceException(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public ErrorResponseDto toErrorResponse() {
        return new ErrorResponseDto(this.status.value(), this.code, this.message);
    }

}
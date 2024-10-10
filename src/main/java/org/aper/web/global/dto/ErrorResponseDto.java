package org.aper.web.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private int status;
    private String code;
    private String message;

    public static ErrorResponseDto fromErrorCode(HttpStatus status, String code, String message) {
        return new ErrorResponseDto(status.value(), code, message);
    }
}

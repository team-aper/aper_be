package org.aper.web.global.dto;

import lombok.Getter;

@Getter
public class ResponseDto<T> {
    private int status;
    private String message;
    private T data;

    public ResponseDto(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseDto<Void> success(String message) {
        return new ResponseDto<>(200, message, null);
    }

    public static <T> ResponseDto<T> success(String message, T data) {
        return new ResponseDto<>(200, message, data);
    }

    public static <T> ResponseDto<T> fail(String message) {
        return new ResponseDto<>(
                400, message, null);
    }

    public static <T> ResponseDto<T> fail(String message, T data) {
        return new ResponseDto<>(
                400, message, data);
    }
}

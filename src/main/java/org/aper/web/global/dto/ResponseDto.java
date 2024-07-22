package org.aper.web.global.dto;

import lombok.Getter;

@Getter
public class ResponseDto<T> {
    private boolean status;
    private String message;
    private T data;

    public ResponseDto(boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }



    public static <T> ResponseDto<T> fail(String message) {
        return new ResponseDto<>(false, message, null);
    }

    public static <T> ResponseDto<T> fail(String message, T data) {
        return new ResponseDto<>(false, message, data);
    }
}

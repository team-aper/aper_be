package org.aper.web.global.dto;

public record ResponseDto<T>(int status, String message, T data) {

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

    public static <T> ResponseDto<T> redirect(int statusCode, String message) {
        return new ResponseDto<>(statusCode, message, null);
    }

}

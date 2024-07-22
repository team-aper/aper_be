package org.aper.web.global.handler;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Common
    INTERNAL_SERVER_ERROR("C001", "내부 서버 오류가 발생했습니다."),
    INVALID_INPUT_VALUE("C002", "유효성 검사 실패."),

    // Auth
    AUTHENTICATION_FAILED("A001", "인증되지 않은 사용자입니다."),
    REFRESH_TOKEN_NOT_EXISTS("A002", "리프레시 토큰을 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN("A003", "유효하지 않은 리프레시 토큰입니다."),
    INVALID_ACCESS_TOKEN("A004", "유효하지 않은 엑세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN("A006", "만료된 리프레시 토큰입니다."),
    EXPIRED_ACCESS_TOKEN("A007", "만료된 엑세스 토큰입니다."),
    ACCESS_TOKEN_IS_NULL("A008", "엑세스 토큰이 존재하지 않습니다."),

    // User
    ALREADY_EXIST_EMAIL("U001", "이미 가입된 이메일입니다."),
    USER_NOT_FOUND("U002", "등록되지 않은 회원입니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
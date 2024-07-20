package org.aper.web.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Common
    INTERNAL_SERVER_ERROR("C001", "내부 서버 오류가 발생했습니다."),
    INVALID_INPUT_VALUE("C002", "유효성 검사 실패"),

    // User
    ALREADY_EXIST_EMAIL("U001", "이미 가입된 이메일입니다.")
    ;

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
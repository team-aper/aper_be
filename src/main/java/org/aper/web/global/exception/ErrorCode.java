package org.aper.web.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR("내부 서버 오류가 발생했습니다."),
    INVALID_INPUT_VALUE("유효성 검사 실패")
    ;

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
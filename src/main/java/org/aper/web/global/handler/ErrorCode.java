package org.aper.web.global.handler;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Common
    INTERNAL_SERVER_ERROR("C001", "내부 서버 오류가 발생했습니다."),
    INVALID_INPUT_VALUE("C002", "유효성 검사 실패."),
    IO_EXCEPTION("C003", "IO Error"),

    // Auth
    AUTHENTICATION_FAILED("A001", "인증에 실패하였습니다."),
    REFRESH_TOKEN_NOT_EXISTS("A002", "리프레시 토큰을 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN("A003", "유효하지 않은 리프레시 토큰입니다."),
    INVALID_ACCESS_TOKEN("A004", "유효하지 않은 엑세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN("A005", "만료된 리프레시 토큰입니다."),
    EXPIRED_ACCESS_TOKEN("A006", "만료된 엑세스 토큰입니다."),
    ACCESS_TOKEN_IS_NULL("A007", "엑세스 토큰이 존재하지 않습니다."),
    BLACK_LISTED_TOKEN("A008", "블랙리스트에 등록된 토큰입니다."),
    AUTH_NOT_FOUND("A009", "사용자의 권한을 찾을 수 없습니다."),

    // User
    ALREADY_EXIST_EMAIL("U001", "이미 가입된 이메일입니다."),
    USER_NOT_FOUND("U002", "등록되지 않은 회원입니다."),
    EMAIL_SEND_FAILURE("U003", "인증코드 전송에 실패하였습니다."),
    EMAIL_AUTH_FAILED("U004", "이메일 인증에 실패하였습니다."),
    PASSWORD_CHANGE_ERROR("U005", "패스워드 변경에 실패하였습니다."),
    INCORRECT_PASSWORD("U006", "비밀번호가 일치하지 않습니다."),

    // Episode
    EPISODE_NOT_FOUND("E001", "존재 하지 않는 에피소드입니다."),
    NOT_AUTHOR_OF_EPISODE("E002", "해당 에피소드의 작성자가 아닙니다."),
    EPISODE_NOT_PUBLISHED("E003", "공개되지 않은 에피소드입니다."),

    // Story
    STORY_NOT_FOUND("S001", "존재 하지 않는 이야기입니다."),
    NOT_AUTHOR_OF_STORY("S002", "해당 이야기의 작성자가 아닙니다."),
    INVALID_ROUTINE("S003", "유효하지 않은 루틴입니다."),
    INVALID_STORY_LINE("S004", "유효하지 않은 줄글 스타일입니다."),
    INVALID_GENRE("S005", "유효하지 않은 장르입니다."),
    STORY_NOT_PUBLISHED("S006", "공개되지 않은 스토리입니다."),

    // AWS S3
    S3_UPLOAD_ERROR_OCCURRED("AWS001", "S3 업로드 중 에러가 발생했습니다."),

    // Elastic
    JSON_PROCESSING_ERROR("ES001", "JSON 변환 중 에러 발생"),
    DOCUMENT_UPDATE_FAILED("ES002", "엘라스틱 서치 데이터 업데이트에 실패했습니다."),
    DOCUMENT_DELETE_FAILED("ES003", "엘라스틱 서치 데이터 삭제에 실패했습니다."),
    ELASTICSEARCH_CONNECT_FAILED("ES004", "엘라스틱 서치에 요청을 실패했습니다."),

    // Paragraph
    PARAGRAPH_NOT_FOUND("P001", "존재하지 않는 문단입니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
package org.aper.web.global.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"C001", "내부 서버 오류가 발생했습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST,"C002", "유효성 검사 실패."),
    IO_EXCEPTION(HttpStatus.BAD_REQUEST, "C003","IO Error"),

    // Auth
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "A001", "인증에 실패하였습니다."),
    REFRESH_TOKEN_IS_NULL(HttpStatus.NOT_FOUND,"A002", "리프레시 토큰을 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.FORBIDDEN,"A003", "유효하지 않은 리프레시 토큰입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.FORBIDDEN,"A004", "유효하지 않은 엑세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.FORBIDDEN,"A005", "만료된 리프레시 토큰입니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED,"A006", "만료된 엑세스 토큰입니다."),
    ACCESS_TOKEN_IS_NULL(HttpStatus.FORBIDDEN,"A007", "엑세스 토큰이 존재하지 않습니다."),
    BLACK_LISTED_TOKEN(HttpStatus.FORBIDDEN,"A008", "블랙리스트에 등록된 토큰입니다."),
    AUTH_NOT_FOUND(HttpStatus.FORBIDDEN,"A009", "사용자의 권한을 찾을 수 없습니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "A010", "로그인 필요. 인증되지 않은 사용자입니다. error : %s"),

    // User
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST,"U001", "이미 가입된 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"U002", "등록되지 않은 회원입니다."),
    EMAIL_SEND_FAILURE(HttpStatus.BAD_REQUEST,"U003", "인증코드 전송에 실패하였습니다."),
    EMAIL_AUTH_FAILED(HttpStatus.BAD_REQUEST,"U004", "이메일 인증에 실패하였습니다."),
    PASSWORD_CHANGE_ERROR(HttpStatus.BAD_REQUEST,"U005", "패스워드 변경에 실패하였습니다."),
    INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST,"U006", "비밀번호가 일치하지 않습니다."),
    INVALID_REVIEW(HttpStatus.BAD_REQUEST, "UH007", "존재하지 않는 종류의 리뷰 입니다."),

    // Episode
    EPISODE_NOT_FOUND(HttpStatus.NOT_FOUND,"E001", "존재 하지 않는 에피소드입니다."),
    NOT_AUTHOR_OF_EPISODE(HttpStatus.FORBIDDEN,"E002", "해당 에피소드의 작성자가 아닙니다."),
    EPISODE_NOT_PUBLISHED(HttpStatus.FORBIDDEN,"E003", "공개되지 않은 에피소드입니다."),

    // Story
    STORY_NOT_FOUND(HttpStatus.NOT_FOUND,"S001", "존재하지 않는 이야기입니다."),
    NOT_AUTHOR_OF_STORY(HttpStatus.FORBIDDEN,"S002", "해당 이야기의 작성자가 아닙니다."),
    INVALID_ROUTINE(HttpStatus.BAD_REQUEST,"S003", "유효하지 않은 루틴입니다."),
    INVALID_STORY_LINE(HttpStatus.BAD_REQUEST,"S004", "유효하지 않은 줄글 스타일입니다."),
    INVALID_GENRE(HttpStatus.BAD_REQUEST,"S005", "유효하지 않은 장르입니다."),
    STORY_NOT_PUBLISHED(HttpStatus.FORBIDDEN,"S006", "공개되지 않은 이야기입니다."),

    // Image
    S3_UPLOAD_ERROR_OCCURRED(HttpStatus.INTERNAL_SERVER_ERROR,"I001", "S3 업로드 중 에러가 발생했습니다."),
    BASE64_DECODE_FAILED(HttpStatus.BAD_REQUEST, "I002", "base64 이미지 디코딩에 실패했습니다."),

    // Elastic
    JSON_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"ES001", "JSON 변환 중 에러 발생"),
    DOCUMENT_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"ES002", "엘라스틱 서치 데이터 업데이트에 실패했습니다."),
    DOCUMENT_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"ES003", "엘라스틱 서치 데이터 삭제에 실패했습니다."),
    ELASTICSEARCH_CONNECT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"ES004", "엘라스틱 서치에 요청을 실패했습니다."),

    // Paragraph
    PARAGRAPH_NOT_FOUND(HttpStatus.NOT_FOUND,"P001", "존재하지 않는 문단입니다."),
    PARAGRAPH_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,"P002", "이미 존재하는 uuid 입니다."),
    PARAGRAPH_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "P003", "이미 삭제된 uuid 입니다."),
    INVALID_TEXT_ALIGN(HttpStatus.BAD_REQUEST, "P004", "유효하지 않은 문단 정렬입니다."),

    // Batch
    INVALID_BATCH_REQUEST(HttpStatus.BAD_REQUEST,"B001", "잘못된 배치 요청입니다."),
    INVALID_BATCH_URL(HttpStatus.BAD_REQUEST,"B002", "잘못된 URL입니다."),
    INVALID_DOMAIN(HttpStatus.BAD_REQUEST, "B003", "잘못된 도메인입니다."),

    // Chat
    TUTOR_NOT_FOUND(HttpStatus.NOT_FOUND,"CH001", "존재하지 않는 튜터입니다."),
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND,"CH002", "존재하지 않는 채팅방입니다."),
    NO_PARTICIPATING_CHAT(HttpStatus.NOT_FOUND, "CH003", "참여 중인 채팅방이 없습니다."),
    CHAT_ALREADY_PARTICIPATING(HttpStatus.BAD_REQUEST, "CH004", "이미 생성된 채팅방 입니다."),
    CHAT_ROOM_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "CH005", "해당 채팅방 형성 요청이 없습니다."),
    CHAT_ROOM_REQUEST_ACCEPTED(HttpStatus.BAD_REQUEST, "CH006", "이미 요청을 수락하셨습니다."),
    CHAT_ROOM_REQUEST_REJECTED(HttpStatus.BAD_REQUEST, "CH007", "이미 요청을 거절하셨습니다."),

    // User History
    HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "UH001", "존재하지 않는 이력 내용입니다."),
    HISTORY_OWNER_MISMATCH(HttpStatus.BAD_REQUEST, "UH002", "해당 이력을 작성 할 수 있는 권한이 없습니다."),
    INVALID_HISTORY(HttpStatus.BAD_REQUEST, "UH003", "존재하지 않는 종류의 이력 입니다."),
    INVALID_ENDDATEVALUE(HttpStatus.BAD_REQUEST, "UH004", "존재하지 않는 종료 형태입니다."),
    INVALID_STARTDATEVALUE(HttpStatus.BAD_REQUEST, "UH005", "존재하지 않는 시작 형태입니다."),

    // Subscription
    SUBSCRIBER_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "존재하지 않는 유저입니다."),
    AUTHOR_NOT_FOUND(HttpStatus.NOT_FOUND, "S002", "존재하지 않는 작가입니다."),
    PUBLISHING_NOT_FOUND(HttpStatus.NOT_FOUND, "S003", "구독 작가의 글이 존재하지 않습니다."),
    SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "S004", "해당하는 구독이 존재하지 않습니다."),

    // OAuth2
    OAUTH2_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "O001", "OAuth2 인증 중 사용자를 찾을 수 없습니다."),
    INVALID_PROVIDER_REQUEST(HttpStatus.BAD_REQUEST, "O002", "Invalid provider: %s"),
    INVALID_FORMAT_REQUEST(HttpStatus.BAD_REQUEST, "O003", "Invalid format Request: %s"),
    REQUEST_IS_NULL(HttpStatus.BAD_REQUEST, "O004", "Original request is null"),

    //Default Enum Type Error
    INVALID_ENUM_TYPE(HttpStatus.BAD_REQUEST, "ET001", "잘못된 종류의 타입입니다.");


    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(HttpStatus status, String code, String message) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

}
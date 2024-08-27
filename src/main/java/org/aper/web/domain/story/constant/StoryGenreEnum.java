package org.aper.web.domain.story.constant;

import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;

public enum StoryGenreEnum {
    DAILY, // 일상
    ROMANCE, // 로맨스
    SF, // SF,
    HORROR, // 공포
    QUEER, // 퀴어
    SOCIETY, // 사회
    ART, // 예술
    CRITICISM, // 비평
    POETRY; //시

    public static StoryGenreEnum fromString(String value) {
        for (StoryGenreEnum genre : StoryGenreEnum.values()) {
            if (genre.name().equalsIgnoreCase(value)) {
                return genre;
            }
        }
        throw new ServiceException(ErrorCode.INVALID_GENRE);
    }
}
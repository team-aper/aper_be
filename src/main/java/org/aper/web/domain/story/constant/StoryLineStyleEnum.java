package org.aper.web.domain.story.constant;

import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;

public enum StoryLineStyleEnum {
    MUNSEOBU_BATANGCHE,//  문서부 바탕체
    KO_PUB_BATANGCHE, // KoPub 바탕체
    NOTO_SERIF_KR, // Noto Serif KR
    NAVER_NANOOM_MYEONGJO; // 네이버 나눔명조

    public static StoryLineStyleEnum fromString(String value) {
        for (StoryLineStyleEnum lineStyle : StoryLineStyleEnum.values()) {
            if (lineStyle.name().equalsIgnoreCase(value)) {
                return lineStyle;
            }
        }
        throw new ServiceException(ErrorCode.INVALID_STORY_LINE);
    }
}
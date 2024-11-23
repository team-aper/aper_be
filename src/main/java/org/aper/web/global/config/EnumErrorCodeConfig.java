package org.aper.web.global.config;

import org.aper.web.domain.paragraph.entity.TextAlignEnum;
import org.aper.web.domain.review.entity.constant.ReviewTypeEnum;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.domain.story.entity.constant.StoryLineStyleEnum;
import org.aper.web.domain.story.entity.constant.StoryRoutineEnum;
import org.aper.web.domain.user.entity.constant.*;
import org.aper.web.global.handler.ErrorCode;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class EnumErrorCodeConfig {
    private final Map<Class<?>, ErrorCode> errorCodeMap = new HashMap<>();

    public EnumErrorCodeConfig() {
        errorCodeMap.put(EndDateTypeEnum.class, ErrorCode.INVALID_ENDDATEVALUE);
        errorCodeMap.put(HistoryTypeEnum.class, ErrorCode.INVALID_HISTORY);
        errorCodeMap.put(StartDateTypeEnum.class, ErrorCode.INVALID_STARTDATEVALUE);
        errorCodeMap.put(UserBatchTypeEnum.class, ErrorCode.INVALID_BATCH_REQUEST);
        errorCodeMap.put(StoryGenreEnum.class, ErrorCode.INVALID_GENRE);
        errorCodeMap.put(StoryLineStyleEnum.class, ErrorCode.INVALID_STORY_LINE);
        errorCodeMap.put(StoryRoutineEnum.class, ErrorCode.INVALID_ROUTINE);
        errorCodeMap.put(ReviewTypeEnum.class, ErrorCode.INVALID_REVIEW);
        errorCodeMap.put(TextAlignEnum.class, ErrorCode.INVALID_TEXT_ALIGN);

    }

    public ErrorCode getErrorCode(Class<?> enumClass) {
        return errorCodeMap.getOrDefault(enumClass, ErrorCode.INVALID_ENUM_TYPE);
    }
}

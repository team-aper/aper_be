package org.aper.web.domain.story.constant;

import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;

public enum StoryRoutineEnum {
    FREE, // 자유루틴
    SHORT, // 단편
    LONG, // 장편
    POETRY; // 시집

    public static StoryRoutineEnum fromString(String value) {
        for (StoryRoutineEnum routine : StoryRoutineEnum.values()) {
            if (routine.name().equalsIgnoreCase(value)) {
                return routine;
            }
        }
        throw new ServiceException(ErrorCode.INVALID_ROUTINE);
    }
}
package org.aper.web.domain.paragraph.entity;

import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;

public enum TextAlignEnum {
    LEFT, // 좌
    RIGHT, // 우
    CENTER; // 중간

    public static TextAlignEnum fromString(String value) {
        for (TextAlignEnum textAlign : TextAlignEnum.values()) {
            if (textAlign.name().equalsIgnoreCase(value)) {
                return textAlign;
            }
        }
        throw new ServiceException(ErrorCode.INVALID_TEXT_ALIGN);
    }
}
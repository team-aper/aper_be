package org.aper.web.domain.review.entity.constant;

import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;

public enum ReviewTypeEnum {
    aa,  // 예시1
    bb,  // 예시2
    cc;  // 예시3

    public static ReviewTypeEnum fromString(String value) {
        for (ReviewTypeEnum type : ReviewTypeEnum.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new ServiceException(ErrorCode.INVALID_REVIEW);
    }
}

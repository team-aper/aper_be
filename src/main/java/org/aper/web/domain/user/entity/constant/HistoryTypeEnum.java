package org.aper.web.domain.user.entity.constant;

import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;

public enum HistoryTypeEnum {
    EDUCATION,  // 학력
    AWARD,      // 수상
    PUBLICATION; // 출간물

    public static HistoryTypeEnum fromString(String value) {
        for (HistoryTypeEnum type : HistoryTypeEnum.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new ServiceException(ErrorCode.INVALID_HISTORY);
    }
}

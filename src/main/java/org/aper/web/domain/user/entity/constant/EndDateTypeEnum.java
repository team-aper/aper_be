package org.aper.web.domain.user.entity.constant;

import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;

public enum EndDateTypeEnum {
    GRADUATED,
    EXPECTED,
    LEAVE,
    ATTENDING,
    COMPLETE,
    DROPED,
    ETC;
    public static EndDateTypeEnum fromString(String value) {
        for (EndDateTypeEnum type : EndDateTypeEnum.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new ServiceException(ErrorCode.INVALID_ENDDATEVALUE);
    }
}

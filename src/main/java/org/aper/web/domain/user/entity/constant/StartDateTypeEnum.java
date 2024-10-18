package org.aper.web.domain.user.entity.constant;

import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;

public enum StartDateTypeEnum {
    ENTERED,
    TRANSFERED;

    public static StartDateTypeEnum fromString(String value) {
        for (StartDateTypeEnum type : StartDateTypeEnum.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new ServiceException(ErrorCode.INVALID_STARTDATEVALUE);
    }
}

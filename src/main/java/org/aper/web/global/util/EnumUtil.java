package org.aper.web.global.util;

import lombok.RequiredArgsConstructor;
import org.aper.web.global.config.EnumErrorCodeConfig;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnumUtil {
    private final EnumErrorCodeConfig errorCodeConfig;

    public <T extends Enum<T>> T fromString(Class<T> enumType, String value) {
        for (T enumConstant : enumType.getEnumConstants()) {
            if (enumConstant.name().equalsIgnoreCase(value)) {
                return enumConstant;
            }
        }
        throw new ServiceException(errorCodeConfig.getErrorCode(enumType));
    }
}

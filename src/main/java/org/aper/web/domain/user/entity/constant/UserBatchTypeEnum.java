package org.aper.web.domain.user.entity.constant;

import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;

public enum UserBatchTypeEnum {
    PENNAME,  // 필명
    IMAGE,      // 이미지
    DESCRIPTION,    //작가의 말
    CONTACTMAIL,    //컨택 메일
    HISTORY,    //이력
    CLASSDESCRIPTION; //1:1수업 소개글

    public static UserBatchTypeEnum fromString(String value) {
        for (UserBatchTypeEnum type : UserBatchTypeEnum.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new ServiceException(ErrorCode.INVALID_BATCH_REQUEST);
    }
}

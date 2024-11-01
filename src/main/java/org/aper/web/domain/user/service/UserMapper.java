package org.aper.web.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.field.service.FieldMapper;
import org.aper.web.domain.user.dto.UserResponseDto.*;
import org.aper.web.domain.user.entity.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final FieldMapper fieldMapper;
    public UserInfo userToUserInfo(User user) {
        return new UserInfo(
                user.getPenName(),
                user.getFieldImage(),
                user.getContactEmail(),
                user.getDescription(),
                fieldMapper.toDetailsResponse(user.getUserHistories()),
                user.getClassDescription()
        );
    }
}

package org.aper.web.domain.user.service;

import com.aperlibrary.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.aper.web.domain.field.service.FieldMapper;
import org.aper.web.domain.user.dto.UserResponseDto.*;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

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

    public YearMonth StringToYearMonth(String yearMonth) {
        return YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyy.MM"));
    }
}

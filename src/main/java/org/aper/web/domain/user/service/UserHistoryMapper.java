package org.aper.web.domain.user.service;

import org.aper.web.domain.user.dto.UserResponseDto.*;
import org.aper.web.domain.user.entity.UserHistory;
import org.aper.web.domain.user.entity.constant.HistoryTypeEnum;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserHistoryMapper {
    public HistoryResponseDto UserHistoryToDto(UserHistory history, HistoryTypeEnum historyType) {
        return new HistoryResponseDto(
                history.getId(),
                historyType.name(),
                history.getDate(),
                historyType == HistoryTypeEnum.EDUCATION ? history.getEndDate() : null,
                history.getDescription()
        );
    }

    public List<HistoryResponseDto> UserHistoriesToDtoList(List<UserHistory> histories) {
        return histories.stream()
                .map(history -> {
                    HistoryTypeEnum historyType = HistoryTypeEnum.fromString(history.getHistoryType().name());
                    return UserHistoryToDto(history, historyType);
                })
                .toList();
    }
}

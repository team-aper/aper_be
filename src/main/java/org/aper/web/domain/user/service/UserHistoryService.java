package org.aper.web.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.field.service.FieldHelper;
import org.aper.web.domain.field.service.FieldMapper;
import org.aper.web.domain.user.dto.UserRequestDto.*;
import org.aper.web.domain.user.dto.UserResponseDto.*;
import org.aper.web.domain.user.entity.UserHistory;
import org.aper.web.domain.user.entity.constant.HistoryTypeEnum;
import org.aper.web.domain.user.repository.UserHistoryRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserHistoryService {
    private final UserHistoryRepository userHistoryRepository;
    private final UserHistoryMapper userHistoryMapper;
    private final FieldHelper fieldHelper;
    private final FieldMapper fieldMapper;

    public List<HistoryResponseDto> changeHistory(UserDetailsImpl userDetails, List<HistoryRequestDto> historyDtoList) {
        List<UserHistory> historiesToSave = historyDtoList.stream()
                .map(historyDto -> {
                    UserHistory history = historyDto.historyId() == null
                            ? new UserHistory()
                            : userHistoryRepository.findById(historyDto.historyId())
                            .orElseThrow(() -> new ServiceException(ErrorCode.HISTORY_NOT_FOUND));

                    if (historyDto.historyId() == null) {
                        history.updateUser(userDetails.user());
                    }

                    if (historyDto.historyId() != null && !history.getUser().getUserId().equals(userDetails.user().getUserId())) {
                        throw new ServiceException(ErrorCode.HISTORY_OWNER_MISMATCH);
                    }

                    HistoryTypeEnum historyType = HistoryTypeEnum.fromString(historyDto.historyType());
                    history.updateHistory(historyDto.date(),
                            historyType == HistoryTypeEnum.EDUCATION ? historyDto.endDate() : null,
                            historyDto.description(),
                            historyType);

                    return history;
                })
                .toList();

        userHistoryRepository.saveAll(historiesToSave);
        return userHistoryMapper.UserHistoriesToDtoList(historiesToSave);
    }
}
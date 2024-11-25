package org.aper.web.domain.user.service;

import com.aperlibrary.user.entity.User;
import com.aperlibrary.user.entity.UserHistory;
import com.aperlibrary.user.entity.constant.EndDateTypeEnum;
import com.aperlibrary.user.entity.constant.HistoryTypeEnum;
import com.aperlibrary.user.entity.constant.StartDateTypeEnum;
import lombok.RequiredArgsConstructor;
import org.aper.web.domain.user.dto.UserRequestDto.*;
import org.aper.web.domain.user.repository.UserHistoryRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.util.EnumUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserHistoryService {
    private final UserHistoryRepository userHistoryRepository;
    private final EnumUtil enumUtil;

    @Transactional
    public void changeHistory(User user, List<HistoryRequestDto> historyDtoList) {
        List<UserHistory> historiesToSave = historyDtoList.stream()
                .map(historyDto -> {
                    UserHistory history = createOrFindHistory(user, historyDto);

                    validateUserOwnership(history, user);

                    HistoryTypeEnum historyType = enumUtil.fromString(HistoryTypeEnum.class, historyDto.historyType());

                    updateHistory(history, historyDto, historyType);

                    return history;
                })
                .toList();

        userHistoryRepository.saveAll(historiesToSave);
    }

    private UserHistory createOrFindHistory(User user, HistoryRequestDto historyDto) {
        if (historyDto.historyId() == null) {
            UserHistory userHistory = new UserHistory();
            userHistory.updateUser(user);
            return userHistory;
        }
        return userHistoryRepository.findById(historyDto.historyId())
                .orElseThrow(() -> new ServiceException(ErrorCode.HISTORY_NOT_FOUND));
    }

    private void validateUserOwnership(UserHistory history, User user) {
        if (history.getUser() != null && !history.getUser().getUserId().equals(user.getUserId())) {
            throw new ServiceException(ErrorCode.HISTORY_OWNER_MISMATCH);
        }
    }

    private void updateHistory(UserHistory history, HistoryRequestDto historyDto, HistoryTypeEnum historyType) {
        if (historyType.equals(HistoryTypeEnum.EDUCATION)) {
            EndDateTypeEnum endDateType = enumUtil.fromString(EndDateTypeEnum.class, historyDto.endDateType());
            StartDateTypeEnum startDateType = enumUtil.fromString(StartDateTypeEnum.class, historyDto.startDateType());
            history.updateEducation(historyDto.date(), historyDto.endDate(), historyDto.description(), endDateType, startDateType);
            return;
        }
        history.updateAwardPublication(historyDto.date(), historyDto.description(), historyType);
    }
}


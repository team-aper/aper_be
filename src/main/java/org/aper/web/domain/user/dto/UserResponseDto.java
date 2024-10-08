package org.aper.web.domain.user.dto;

import java.time.LocalDate;

public class UserResponseDto {

    public record SignupResponseDto(
            String penName
    ) {}

    public record HistoryResponseDto(
            Long historyId,
            String historyType,
            LocalDate date,
            LocalDate endDate,
            String description
    ) {}
}

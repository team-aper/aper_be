package org.aper.web.domain.user.dto;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class UserResponseDto {

    public record SignupResponseDto(
            String penName
    ) {}

    public record HistoryResponseDto(
            List<HistoryDetailResponseDto> educationResponseDtoList,
            List<HistoryDetailResponseDto> awardResponseDtoList,
            List<HistoryDetailResponseDto> publicationResponseList
    ) {}

    public record HistoryDetailResponseDto(
            Long historyId,
            String historyType,
            YearMonth date,
            YearMonth endDate,
            String startDateType,
            String endDateType,
            String description
    ) {}

    public record ClassDescriptionResponseDto(
            String classDescription
    ) {}
}

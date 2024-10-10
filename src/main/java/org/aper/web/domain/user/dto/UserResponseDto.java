package org.aper.web.domain.user.dto;

import java.time.LocalDate;
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
            LocalDate date,
            LocalDate endDate,
            String description
    ) {}

    public record ClassDescriptionResponseDto(
            String classDescription
    ) {}
}

package org.aper.web.domain.user.dto;

import java.time.LocalDate;
import java.util.List;

public class UserResponseDto {

    public record SignupResponseDto(
            String penName
    ) {}

    public record HistoryOwnershipResponseDto(
            List<HistoryResponseDto> educationResponseDtoList,
            List<HistoryResponseDto> awardResponseDtoList,
            List<HistoryResponseDto> publicationResponseList
    ) {}

    public record HistoryResponseDto(
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

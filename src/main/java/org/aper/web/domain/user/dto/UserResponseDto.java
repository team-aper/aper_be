package org.aper.web.domain.user.dto;

import java.time.LocalDate;

public class UserResponseDto {

    public record SignupResponseDto(
            String penName
    ) {}

    public record HistoryEducationDto(
            LocalDate date,
            LocalDate endDate,
            String description
    ) {}

    public record HistoryAwardDto(
            LocalDate date,
            String description
    ) {}

    public record HistoryPublicationDto(
            LocalDate date,
            String description
    ) {}
}

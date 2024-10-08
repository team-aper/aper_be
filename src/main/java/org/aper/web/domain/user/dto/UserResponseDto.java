package org.aper.web.domain.user.dto;

import java.time.LocalDateTime;

public class UserResponseDto {

    public record SignupResponseDto(
            String penName
    ) {}

    public record HistoryEducationDto(
            LocalDateTime date,
            LocalDateTime endDate,
            String description
    ) {}

    public record HistoryAwardDto(
            LocalDateTime date,
            String description
    ) {}

    public record HistoryPublicationDto(
            LocalDateTime date,
            String description
    ) {}
}

package org.aper.web.domain.user.dto;

import org.aper.web.domain.user.entity.constant.ReviewTypeEnum;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public class UserResponseDto {

    public record UserInfo(
            String penName,
            String fieldImage,
            String contactMail,
            String description,
            HistoryResponseDto historyList,
            String classDescription
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
            String classDescription,
            Long totalClasses,
            Long reviewers,
            Map<ReviewTypeEnum, Long> reviewDetails
    ) {}

    public record CreatedReviewDto(
            Long reviewId
    ) {}

    public record IsDuplicated(
            boolean isDuplicated
    ) {}
}

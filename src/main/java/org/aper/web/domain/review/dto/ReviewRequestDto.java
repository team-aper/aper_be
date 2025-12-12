package org.aper.web.domain.review.dto;

import org.aper.web.domain.common.constant.ReviewTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ReviewRequestDto {
    public record CreateReviewRequestDto(
            @NotNull(message = "revieweeId is null")
            Long revieweeId,

            @NotEmpty(message = "reviewTypes is null or empty")
            List<ReviewTypeEnum> reviewTypes,

            @NotNull(message = "chatRoomId is null")
            Long chatRoomId
    ) {}
}

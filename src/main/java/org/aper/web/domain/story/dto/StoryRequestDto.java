package org.aper.web.domain.story.dto;

import jakarta.validation.constraints.NotBlank;

public class StoryRequestDto {

    public record StoryCreateDto(
            @NotBlank(message = "이야기 제목을 입력해주세요.")
            String title,
            @NotBlank(message = "루틴을 입력해주세요.")
            String routine,
            @NotBlank(message = "장르를 입력해주세요.")
            String genre,
            @NotBlank(message = "줄글 스타일을 입력해주세요.")
            String lineStyle
    )
    {}
}

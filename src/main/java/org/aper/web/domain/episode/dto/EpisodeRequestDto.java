package org.aper.web.domain.episode.dto;

import jakarta.validation.constraints.NotBlank;

public class EpisodeRequestDto {

    public record TitleChangeDto(
            @NotBlank(message = "제목을 입력해주세요.")
            String title
    ){}

    public record TextChangeDto(
            String text
    ){}

    public record DeleteEpisodeDto(
            @NotBlank(message = "chapter is null")
            Long chapter,
            @NotBlank(message = "storyId is null")
            Long storyId
    ){}
}

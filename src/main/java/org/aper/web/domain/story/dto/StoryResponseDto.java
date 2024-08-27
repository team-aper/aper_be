package org.aper.web.domain.story.dto;

import java.time.LocalDateTime;
import java.util.List;

public class StoryResponseDto {

    public record GetStoryDto(
            String coverTitle,
            String routineType,
            String penName,
            String genre,
            String lineStyle,
            LocalDateTime createdDate,
            LocalDateTime publicDate,
            boolean isPublished,
            List<EpisodeResponseDto> episodes
    ){}

    public record EpisodeResponseDto(
            String title,
            Long chapter,
            String description,
            LocalDateTime createDate,
            LocalDateTime publicDate,
            boolean isPublished,
            String dDay
    ) {
    }
}

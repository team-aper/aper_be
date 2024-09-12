package org.aper.web.domain.episode.dto;

import java.time.LocalDateTime;

public class EpisodeResponseDto {

    public record CreatedEpisodeDto(
            Long episodeId,
            String title,
            Long chapter,
            String description,
            LocalDateTime createDate,
            LocalDateTime publicDate,
            boolean isPublished,
            String dDay
    ) {
    }

    public record EpisodeHeaderDto(
            Long episodeId,
            Long authorId,
            Long storyId,
            String storyTitle,
            String episodeTitle,
            Long chapter,
            String genre,
            LocalDateTime createdDate,
            LocalDateTime publicDate,
            boolean isPublished
    ){}

    public record EpisodeTextDto(
            String text
    ){}
}

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
}

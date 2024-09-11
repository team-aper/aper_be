package org.aper.web.domain.kafka.entity.dto;

import java.time.LocalDateTime;

public class KafkaDto {
    public record KafkaEpisodeDto(
            Long episodeId,
            Long episodeChapter,
            String episodeTitle,
            String episodeDescription,
            LocalDateTime episodePublicDate,
            boolean episodeOnDisplay,
            Long storyId,
            String storyTitle,
            String storyGenre,
            boolean storyOnDisplay,
            Long userId,
            String penName,
            String fieldImage,
            String operation
    ) {}

    public record KafkaUserDto(
            Long userId,
            String penName,
            String operation
    ) {}
}

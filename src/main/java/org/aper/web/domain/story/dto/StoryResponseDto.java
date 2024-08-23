package org.aper.web.domain.story.dto;

import org.aper.web.domain.episode.entity.Episode;

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
            List<Episode> episodes
    ){}
}

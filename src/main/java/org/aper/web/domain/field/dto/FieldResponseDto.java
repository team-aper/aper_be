package org.aper.web.domain.field.dto;

import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.domain.story.entity.constant.StoryRoutineEnum;

import java.time.LocalDateTime;
import java.util.List;

public class FieldResponseDto {

    public record FieldHeaderResponseDto(
            String penName,
            String fieldImageUrl,
            String description
    ) {}

    public record HomeResponseDto(
            boolean isMyField,
            List<HomeDetailsResponseDto> detailsList
    ) {}

    public record HomeDetailsResponseDto(
            Long storyId,
            String storyTitle,
            Long episodeId,
            String episodeTitle,
            StoryGenreEnum storyGenreEnum,
            LocalDateTime date,
            boolean isPublished
    ) {}

    public record StoriesResponseDto(
            boolean isMyField,
            List<StoriesDetailsResponseDto> storiesList
    ) {}

    public record StoriesDetailsResponseDto(
            Long storyId,
            String storyTitle,
            StoryRoutineEnum storyRoutineEnum,
            StoryGenreEnum storyGenreEnum,
            LocalDateTime date,
            boolean isPublished
    ) {}

    public record DetailsResponseDto(
            String penName,
            String email
    ) {}

}

package org.aper.web.domain.field.dto;

import java.time.LocalDateTime;
import java.util.List;

public class FieldResponseDto {

    public record FieldHeaderResponseDto(
            String penName,
            String fieldImageUrl,
            String description,
            String contactEmail
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
            Long chapter,
            String genre,
            LocalDateTime date,
            String text,
            boolean isPublished
    ) {}

    public record StoriesResponseDto(
            boolean isMyField,
            List<StoriesDetailsResponseDto> storiesList
    ) {}

    public record StoriesDetailsResponseDto(
            Long storyId,
            String storyTitle,
            String routine,
            String genre,
            String lineStyle,
            LocalDateTime date,
            boolean isPublished
    ) {}

    public record DetailsResponseDto(
            String penName,
            String email
    ) {}

}

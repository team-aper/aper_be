package org.aper.web.domain.story.dto;

public class StoryRequestDto {

    public record StoryCreateDto(
            String title,
            String routine,
            String genre,
            String lineStyle
    )
    {}
}

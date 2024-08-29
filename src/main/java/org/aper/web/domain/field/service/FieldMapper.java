package org.aper.web.domain.field.service;

import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.field.dto.FieldResponseDto.*;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FieldMapper {

    public HomeDetailsResponseDto toHomeDetailsResponseDto(Episode episode) {
        return new HomeDetailsResponseDto(
                episode.getStory().getId(),
                episode.getStory().getTitle(),
                episode.getId(),
                episode.getTitle(),
                episode.getStory().getGenre(),
                episode.getPublicDate(),
                episode.isOnDisplay()
        );
    }

    public StoriesDetailsResponseDto toStoriesDetailsResponseDto(Story story) {
        return new StoriesDetailsResponseDto(
                story.getId(),
                story.getTitle(),
                story.getRoutine(),
                story.getGenre(),
                story.getPublicDate(),
                story.isOnDisplay()
        );
    }

    public DetailsResponseDto toDetailsResponseDto(User user) {
        return new DetailsResponseDto(
                user.getPenName(),
                user.getEmail()
        );
    }

    public List<HomeDetailsResponseDto> toHomeDetailsResponseDtoList(List<Episode> episodes) {
        return episodes.stream()
                .map(this::toHomeDetailsResponseDto)
                .collect(Collectors.toList());
    }

    public List<StoriesDetailsResponseDto> toStoriesDetailsResponseDtoList(List<Story> stories) {
        return stories.stream()
                .map(this::toStoriesDetailsResponseDto)
                .collect(Collectors.toList());
    }
}

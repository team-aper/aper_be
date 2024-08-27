package org.aper.web.domain.field.dto;

import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.domain.story.entity.constant.StoryRoutineEnum;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.user.entity.User;

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
    ) {
        public HomeDetailsResponseDto(Episode episode) {
            this(
                    episode.getStory().getId(),
                    episode.getStory().getTitle(),
                    episode.getId(),
                    episode.getTitle(),
                    episode.getStory().getGenre(),
                    episode.getPublicDate(),
                    episode.isOnDisplay()
            );
        }
    }

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
    ) {
        public StoriesDetailsResponseDto(Story story) {
            this(
                    story.getId(),
                    story.getTitle(),
                    story.getRoutine(),
                    story.getGenre(),
                    story.getPublicDate(),
                    story.isOnDisplay()
            );
        }
    }

    public record DetailsResponseDto(
            String penName,
            String email
    ) {
        public DetailsResponseDto(User user) {
            this(
                    user.getPenName(),
                    user.getEmail()
            );
        }
    }

}

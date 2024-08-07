package org.aper.web.domain.field.dto;

import lombok.Getter;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.story.constant.StoryGenreEnum;

import java.sql.Timestamp;

@Getter
public class HomeDetailsResponseDto {
    private Long storyId;
    private String storyTitle;
    private Long episodeId;
    private String episodeTitle;
    private StoryGenreEnum storyGenreEnum;
    private Timestamp date;
    private boolean isMyField;
    private boolean isPublished;

    public HomeDetailsResponseDto(Episode episode) {
        this.storyId = episode.getStory().getId();
        this.storyTitle = episode.getStory().getTitle();
        this.episodeId = episode.getId();
        this.episodeTitle = episode.getTitle();
        this.storyGenreEnum = episode.getStory().getGenre();
        this.date = episode.getPublicDate();
        this.isPublished = episode.getOnDisplay();
    }
}

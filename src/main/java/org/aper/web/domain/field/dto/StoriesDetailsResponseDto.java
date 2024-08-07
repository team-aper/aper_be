package org.aper.web.domain.field.dto;

import lombok.Getter;
import org.aper.web.domain.story.constant.StoryGenreEnum;
import org.aper.web.domain.story.constant.StoryRoutineEnum;
import org.aper.web.domain.story.entity.Story;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
public class StoriesDetailsResponseDto {
    private Long storyId;
    private String storyTitle;
    private StoryRoutineEnum storyRoutineEnum;
    private StoryGenreEnum storyGenreEnum;
    private LocalDateTime date;
    private boolean isPublished;

    public StoriesDetailsResponseDto(Story story) {
        this.storyId = story.getId();
        this.storyTitle = story.getTitle();
        this.storyRoutineEnum = story.getRoutine();
        this.storyGenreEnum = story.getGenre();
        this.date = story.getPublicDate();
        this.isPublished = story.isOnDisplay();
    }
}

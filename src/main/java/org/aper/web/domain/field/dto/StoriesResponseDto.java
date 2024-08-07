package org.aper.web.domain.field.dto;

import org.aper.web.domain.story.constant.StoryGenreEnum;
import org.aper.web.domain.story.constant.StoryRoutineEnum;

import java.sql.Timestamp;

public class StoriesResponseDto {
    private Long storyId;
    private String storyTitle;
    private StoryRoutineEnum storyRoutineEnum;
    private StoryGenreEnum storyGenreEnum;
    private Timestamp date;
    private boolean isMyField;
    private boolean isPublished;
}

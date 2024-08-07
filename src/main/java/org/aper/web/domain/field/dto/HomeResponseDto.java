package org.aper.web.domain.field.dto;

import lombok.Getter;
import org.aper.web.domain.story.constant.StoryGenreEnum;

import java.sql.Timestamp;

@Getter
public class HomeResponseDto {
    private Long storyId;
    private String storyTitle;
    private Long episodeId;
    private String episodeTitle;
    private StoryGenreEnum storyGenreEnum;
    private Timestamp date;
    private boolean isMyField;
    private boolean isPublished;
}

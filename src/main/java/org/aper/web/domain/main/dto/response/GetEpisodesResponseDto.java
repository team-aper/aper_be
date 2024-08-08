package org.aper.web.domain.main.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.story.constant.StoryGenreEnum;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class GetEpisodesResponseDto {
    private Long episodeId;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime publicDate;
    private Long storyId;
    private String storyTitle;
    private StoryGenreEnum genre;
    private Long userId;
    private String penName;
    private String fieldImage;

    @Builder
    public GetEpisodesResponseDto(Episode episode) {
        this.episodeId = episode.getId();
        this.description = episode.getDescription();
        this.createdAt = episode.getCreatedAt();
        this.publicDate = episode.getPublicDate();
        this.storyId = episode.getStory().getId();
        this.storyTitle = episode.getStory().getTitle();
        this.genre = episode.getStory().getGenre();
        this.userId = episode.getStory().getUser().getUserId();
        this.penName = episode.getStory().getUser().getPenName();
        this.fieldImage = episode.getStory().getUser().getFieldImage();
    }

    public static GetEpisodesResponseDto of(Episode episode) {
        return GetEpisodesResponseDto.builder().episode(episode).build();
    }
}

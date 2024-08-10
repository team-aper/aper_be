package org.aper.web.domain.main.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aper.web.domain.curation.entity.Curation;
import org.aper.web.domain.story.constant.StoryGenreEnum;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class GetCurationsResponseDto {
    private Long curationId;
    private String content;
    private Long episodeId;
    private LocalDateTime publicDate;
    private Long storyId;
    private StoryGenreEnum genre;
    private String storyTitle;
    private String penName;

    @Builder
    public GetCurationsResponseDto(Curation curation) {
        this.curationId = curation.getId();
        this.content  = curation.getContent();
        this.episodeId = curation.getEpisode().getId();
        this.publicDate = curation.getEpisode().getPublicDate();
        this.storyId = curation.getEpisode().getStory().getId();
        this.genre = curation.getEpisode().getStory().getGenre();
        this.storyTitle = curation.getEpisode().getStory().getTitle();
        this.penName = curation.getEpisode().getStory().getUser().getPenName();

    }

    public static GetCurationsResponseDto of(Curation curation) {
        return GetCurationsResponseDto.builder().curation(curation).build();
    }
}

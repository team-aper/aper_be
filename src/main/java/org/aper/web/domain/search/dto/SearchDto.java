package org.aper.web.domain.search.dto;

import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.story.constant.StoryGenreEnum;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class SearchDto {

    public record SearchStoryResponseDto(
            List<StoryListResponseDto> storyList
    ) {}

    public record StoryListResponseDto(
       Long storyId,
       String storyTitle,
       Long authorId,
       StoryGenreEnum storyGenre,
       LocalDateTime publicDate,
       Long episodeId,
       String episodeFirstParagraph
    ) {
        public StoryListResponseDto(
                Long storyId,
                String storyTitle,
                Long authorId,
                StoryGenreEnum storyGenre,
                LocalDateTime publicDate,
                Long episodeId,
                String episodeFirstParagraph) {
            this.storyId = storyId;
            this.storyTitle = storyTitle;
            this.authorId = authorId;
            this.storyGenre = storyGenre;
            this.publicDate = publicDate;
            this.episodeId = episodeId;
            this.episodeFirstParagraph = episodeFirstParagraph;
        }
    }

    public record SearchAuthorResponseDto(
            List<AuthorListResponseDto> authorList
    ) {}

    public record AuthorListResponseDto(
            String penName,
            String fieldImageUrl,
            String description,
            Long authorId
    ) {
        public AuthorListResponseDto(String penName, String fieldImageUrl, String description, Long authorId) {
            this.penName = penName;
            this.fieldImageUrl = fieldImageUrl;
            this.description = description;
            this.authorId = authorId;
        }
    }
}

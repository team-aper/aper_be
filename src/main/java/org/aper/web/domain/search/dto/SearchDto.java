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
       StoryGenreEnum StoryGenre,
       LocalDateTime publicDate,
       Long episodeId,
       String episodeFirstParagraph
    ) {
        public StoryListResponseDto(Story story, Episode episode, String paragraph) {
            this(
                    story.getId(),
                    story.getTitle(),
                    story.getUser().getUserId(),
                    story.getGenre(),
                    story.getPublicDate(),
                    episode.getId(),
                    paragraph
            );
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
        public AuthorListResponseDto(User user) {
            this(
                    user.getPenName(),
                    user.getFieldImage(),
                    user.getDescription(),
                    user.getUserId()
            );
        }
    }
}

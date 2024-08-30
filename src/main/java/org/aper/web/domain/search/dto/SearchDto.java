package org.aper.web.domain.search.dto;

import org.aper.web.domain.story.entity.constant.StoryGenreEnum;

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
       String storyGenre,
       LocalDateTime publicDate,
       Long episodeId,
       String episodeFirstParagraph
    ) {}

    public record SearchAuthorResponseDto(
            List<AuthorListResponseDto> authorList
    ) {}

    public record AuthorListResponseDto(
            String penName,
            String fieldImageUrl,
            String description,
            Long authorId
    ) {}
}

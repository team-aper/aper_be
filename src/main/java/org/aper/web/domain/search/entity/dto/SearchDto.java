package org.aper.web.domain.search.entity.dto;

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
           String episodeId,
           String episodeFirstParagraph
    ) {}

    public record AuthorPenNameResponseDto(
            String penName
    ) {}

    public record SearchAuthorResponseDto(
            List<AuthorListResponseDto> authorList
    ) {}

    public record SearchPenNameResponseDto(
            List<AuthorPenNameResponseDto> authorList
    ) {}

    public record AuthorListResponseDto(
            String penName,
            String fieldImageUrl,
            String description,
            Long authorId,
            List<AuthorStoryListResponseDto> storyList,
            Long reviewers,
            Long subscribers
    ) {}

    public record AuthorStoryListResponseDto(
            String storyTitle,
            Long storyId
    ) {}
}

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
           Long userId,
           String genre,
           LocalDateTime publicDate,
           String episodeId,
           String description,
           String penName,
           String fieldImage
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
            Long subscribers,
            boolean isSubscribed
    ) {}

    public record AuthorStoryListResponseDto(
            String storyTitle,
            Long storyId
    ) {}
}

package org.aper.web.domain.search.dto;

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
            String fieldImage,
            String description,
            Long authorId,
            List<AuthorStoryListResponseDto> storyList,
            Long reviewers,
            Long subscribers,
            boolean isSubscribed // TODO: 이게 어떤 것을 나타내는 필드지?
    ) {}

    public record AuthorStoryListResponseDto(
            String storyTitle,
            Long storyId
    ) {}
}

package org.aper.web.domain.main.dto;

import org.aper.web.domain.story.entity.constant.StoryGenreEnum;

import java.time.LocalDateTime;
import java.util.List;

public class MainResponseDto {

    public record GetEpisodesListResponseDto(
            List<GetEpisodesResponseDto> episodes
    ) {
    }

    public record GetUsersListResponseDto(
            List<GetUsersResponseDto> users
    ) {
    }

    public record GetCurationsListResponseDto(
            List<GetCurationsResponseDto> curations
    ) {
    }

    public record GetEpisodesResponseDto(
            Long episodeId,
            String description,
            LocalDateTime publicDate,
            Long storyId,
            String storyTitle,
            StoryGenreEnum genre,
            Long userId,
            String penName,
            String fieldImage
    ) {}

    public record GetUsersResponseDto(
            Long userId,
            String penName,
            String fieldImage,
            String description
    ) {}

    public record GetCurationsResponseDto(
            Long curationId,
            String content,
            Long episodeId,
            LocalDateTime publicDate,
            Long storyId,
            StoryGenreEnum genre,
            String storyTitle,
            String penName
    ) {}
}

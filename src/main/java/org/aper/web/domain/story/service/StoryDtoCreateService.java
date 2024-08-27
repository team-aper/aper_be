package org.aper.web.domain.story.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.service.EpisodeService;
import org.aper.web.domain.story.dto.StoryResponseDto;
import org.aper.web.domain.story.dto.StoryResponseDto.EpisodeResponseDto;
import org.aper.web.domain.story.dto.StoryResponseDto.GetStoryDto;
import org.aper.web.domain.story.entity.Story;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryDtoCreateService {

    private final EpisodeService episodeService;

    public GetStoryDto createGetStoryDtoWithEpisodes(Story story) {

        List<EpisodeResponseDto> myEpisodes = episodeService.getEpisodesWithDDay(story.getId());

        return new StoryResponseDto.GetStoryDto(
                story.getTitle(),
                story.getRoutine().name(),
                story.getUser().getPenName(),
                story.getGenre().name(),
                story.getLineStyle().name(),
                story.getCreatedAt(),
                story.getPublicDate(),
                story.isOnDisplay(),
                myEpisodes
        );
    }

    public GetStoryDto createGetStoryDtoWithPublishedEpisodes(Story story) {

        List<EpisodeResponseDto> publishedEpisodes = episodeService.getPublishedEpisodesWithDDay(story.getUser().getUserId());

        return new StoryResponseDto.GetStoryDto(
                story.getTitle(),
                story.getRoutine().name(),
                story.getUser().getPenName(),
                story.getGenre().name(),
                story.getLineStyle().name(),
                story.getCreatedAt(),
                story.getPublicDate(),
                story.isOnDisplay(),
                publishedEpisodes
        );
    }
}

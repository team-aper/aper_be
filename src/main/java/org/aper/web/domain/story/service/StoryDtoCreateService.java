package org.aper.web.domain.story.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.service.EpisodeService;
import org.aper.web.domain.story.dto.StoryResponseDto;
import org.aper.web.domain.story.entity.Story;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryDtoCreateService {

    EpisodeService episodeService;

    public StoryResponseDto.GetStoryDto createGetStoryDtoWithEpisodes(Story story) {

        List<StoryResponseDto.EpisodeResponseDto> myEpisodes = episodeService.getEpisodesWithDDay(story.getId());

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

    public StoryResponseDto.GetStoryDto createGetStoryDtoWithPublishedEpisodes(Story story) {

        List<StoryResponseDto.EpisodeResponseDto> publishedEpisodes = episodeService.getPublishedEpisodesWithDDay(story.getUser().getUserId());

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

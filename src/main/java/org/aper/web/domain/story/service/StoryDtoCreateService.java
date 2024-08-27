package org.aper.web.domain.story.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.dto.EpisodeResponseDto.CreatedEpisodeDto;
import org.aper.web.domain.episode.service.EpisodeService;
import org.aper.web.domain.story.dto.StoryResponseDto.GetStoryDto;
import org.aper.web.domain.story.entity.Story;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryDtoCreateService {

    private final EpisodeService episodeService;

    public GetStoryDto createGetStoryDtoWithEpisodes(Story story) {

        List<CreatedEpisodeDto> myEpisodes = episodeService.getEpisodesWithDDay(story.getId());

        return new GetStoryDto(
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

        List<CreatedEpisodeDto> publishedEpisodes = episodeService.getPublishedEpisodesWithDDay(story.getUser().getUserId());

        return new GetStoryDto(
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

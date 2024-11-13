package org.aper.web.domain.story.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.dto.EpisodeResponseDto.CreatedEpisodeDto;
import org.aper.web.domain.episode.service.EpisodeMapper;
import org.aper.web.domain.story.dto.StoryResponseDto.GetStoryDto;
import org.aper.web.domain.story.entity.Story;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StoryMapper {

    private final EpisodeMapper episodeDtoCreateService;

    public GetStoryDto createGetStoryDtoWithEpisodes(Story story) {

        List<CreatedEpisodeDto> myEpisodes = episodeDtoCreateService.getEpisodesWithDDay(story.getId());
        LocalDateTime date = story.isOnDisplay() ? story.getPublicDate() : story.getCreatedAt();

        return new GetStoryDto(
                story.getTitle(),
                story.getRoutine().name(),
                story.getUser().getPenName(),
                story.getGenre().name(),
                story.getLineStyle().name(),
                date,
                story.isOnDisplay(),
                myEpisodes
        );
    }

    public GetStoryDto createGetStoryDtoWithPublishedEpisodes(Story story) {

        List<CreatedEpisodeDto> publishedEpisodes = episodeDtoCreateService.getPublishedEpisodesWithDDay(story.getId());

        return new GetStoryDto(
                story.getTitle(),
                story.getRoutine().name(),
                story.getUser().getPenName(),
                story.getGenre().name(),
                story.getLineStyle().name(),
                story.getPublicDate(),
                story.isOnDisplay(),
                publishedEpisodes
        );
    }
}

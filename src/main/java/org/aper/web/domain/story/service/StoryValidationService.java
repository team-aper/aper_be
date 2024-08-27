package org.aper.web.domain.story.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.service.EpisodeService;
import org.aper.web.domain.story.dto.StoryResponseDto.EpisodeResponseDto;
import org.aper.web.domain.story.dto.StoryResponseDto.GetStoryDto;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.story.repository.StoryRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryValidationService {
    private final StoryRepository storyRepository;
    public final EpisodeService episodeService;

    public Story validateStoryOwnership(Long storyId, UserDetailsImpl userDetails) {
        Story story = storyRepository.findByStoryAuthor(storyId).orElseThrow(() ->
                new ServiceException(ErrorCode.STORY_NOT_FOUND)
        );

        if (!story.getUser().getUserId().equals(userDetails.user().getUserId())) {
            throw new ServiceException(ErrorCode.NOT_AUTHOR_OF_STORY);
        }

        return story;
    }

    public Story validateStoryAccessibility(Long storyId, UserDetailsImpl userDetails) {
        return storyRepository.findByStoryAuthor(storyId).orElseThrow(() ->
                new ServiceException(ErrorCode.STORY_NOT_FOUND)
        );
    }

    public boolean isOwnStory(Long storyId, UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return false;
        }
        return storyRepository.existsByIdAndUser_UserId(storyId, userDetails.user().getUserId());
    }

    public GetStoryDto createGetStoryDtoWithEpisodes(Story story) {

        List<EpisodeResponseDto> myEpisodes = episodeService.getEpisodesWithDDay(story.getId());

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

        List<EpisodeResponseDto> publishedEpisodes = episodeService.getPublishedEpisodesWithDDay(story.getUser().getUserId());

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

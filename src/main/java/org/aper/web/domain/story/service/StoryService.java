package org.aper.web.domain.story.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.service.EpisodeService;
import org.aper.web.domain.story.constant.StoryGenreEnum;
import org.aper.web.domain.story.constant.StoryLineStyleEnum;
import org.aper.web.domain.story.constant.StoryRoutineEnum;
import org.aper.web.domain.story.dto.StoryRequestDto.StoryCreateDto;
import org.aper.web.domain.story.dto.StoryResponseDto.GetStoryDto;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.story.repository.StoryRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoryService {
    private final StoryRepository storyRepository;
    private final EpisodeService episodeService;
    private final StoryValidationService storyValidationService;

    @Transactional
    public void changePublicStatus(Long storyId, UserDetailsImpl userDetails) {
        Story existStory = storyValidationService.validateStoryOwnership(storyId, userDetails);
        existStory.updateOnDisplay();
        storyRepository.save(existStory);
    }

    @Transactional
    public void createStory(UserDetailsImpl userDetails, StoryCreateDto storyCreateDto) {
        StoryRoutineEnum routineEnum = StoryRoutineEnum.fromString(storyCreateDto.routine());

        Story story = Story.builder()
                .title(storyCreateDto.title())
                .routine(routineEnum)
                .genre(StoryGenreEnum.fromString(storyCreateDto.genre()))
                .lineStyle(StoryLineStyleEnum.fromString(storyCreateDto.lineStyle()))
                .user(userDetails.user())
                .build();

        List<Episode> episodes = episodeService.createEpisodeList(routineEnum, story);
        story.addEpisodes(episodes);

        storyRepository.save(story);
    }

    @Transactional(readOnly = true)
    public GetStoryDto getStory(UserDetailsImpl userDetails, Long storyId) {
        Story story = storyValidationService.validateStoryAccessibility(storyId, userDetails);

        if (storyValidationService.isOwnStory(storyId, userDetails)) {
            return storyValidationService.createGetStoryDtoWithEpisodes(story);
        }

        if (!story.isOnDisplay()) {
            throw new ServiceException(ErrorCode.STORY_NOT_PUBLISHED);
        }

        return storyValidationService.createGetStoryDtoWithPublishedEpisodes(story);
    }
}

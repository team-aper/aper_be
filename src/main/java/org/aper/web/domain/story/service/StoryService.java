package org.aper.web.domain.story.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.episode.dto.EpisodeResponseDto.CreatedEpisodeDto;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.episode.service.EpisodeDtoCreateService;
import org.aper.web.domain.search.service.KafkaProducerService;
import org.aper.web.domain.story.dto.StoryRequestDto;
import org.aper.web.domain.story.dto.StoryRequestDto.StoryCreateDto;
import org.aper.web.domain.story.dto.StoryResponseDto.CreatedStoryDto;
import org.aper.web.domain.story.dto.StoryResponseDto.GetStoryDto;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.domain.story.entity.constant.StoryLineStyleEnum;
import org.aper.web.domain.story.entity.constant.StoryRoutineEnum;
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
    private final EpisodeDtoCreateService episodeDtoCreateService;
    private final StoryValidationService storyValidationService;
    private final StoryDtoCreateService storyDtoCreateService;
    private final EpisodeRepository episodeRepository;
    private final KafkaProducerService kafkaProducerService;

    @Transactional
    public void changePublicStatus(Long storyId, UserDetailsImpl userDetails) {
        Story existStory = storyValidationService.validateStoryOwnership(storyId, userDetails);
        existStory.updateOnDisplay();
        storyRepository.save(existStory);
    }

    @Transactional
    public CreatedStoryDto createStory(UserDetailsImpl userDetails, StoryCreateDto storyCreateDto) {
        StoryRoutineEnum routineEnum = StoryRoutineEnum.fromString(storyCreateDto.routine());

        Story story = Story.builder()
                .title(storyCreateDto.title())
                .routine(routineEnum)
                .genre(StoryGenreEnum.fromString(storyCreateDto.genre()))
                .lineStyle(StoryLineStyleEnum.fromString(storyCreateDto.lineStyle()))
                .user(userDetails.user())
                .build();

        List<Episode> episodes = episodeDtoCreateService.createEpisodeList(routineEnum, story);
        story.addEpisodes(episodes);

        storyRepository.save(story);

        return new CreatedStoryDto(story.getId());
    }

    @Transactional(readOnly = true)
    public GetStoryDto getStory(UserDetailsImpl userDetails, Long storyId) {
        Story story = storyValidationService.validateStoryAccessibility(storyId);

        if (storyValidationService.isOwnStory(storyId, userDetails)) {
            return storyDtoCreateService.createGetStoryDtoWithEpisodes(story);
        }

        if (!story.isOnDisplay()) {
            throw new ServiceException(ErrorCode.STORY_NOT_PUBLISHED);
        }

        return storyDtoCreateService.createGetStoryDtoWithPublishedEpisodes(story);
    }

    @Transactional
    public void changeCover(UserDetailsImpl userDetails, Long storyId, StoryRequestDto.CoverChangeDto coverChangeDto) {
        Story story =  storyValidationService.validateStoryOwnership(storyId, userDetails);
        story.updateCover(
                coverChangeDto.title(),
                StoryGenreEnum.fromString(coverChangeDto.genre()),
                StoryLineStyleEnum.fromString(coverChangeDto.lineStyle())
        );
        storyRepository.save(story);
    }

    @Transactional
    public void deleteStory(UserDetailsImpl userDetails, Long storyId) {
        Story story = storyValidationService.validateStoryOwnership(storyId, userDetails);
        storyRepository.deleteEpisodesByStoryId(story.getId());
        storyRepository.deleteById(story.getId());;
    }

    public CreatedEpisodeDto createEpisode(UserDetailsImpl userDetails, Long storyId, Long chapter) {
        Story story = storyValidationService.validateStoryOwnership(storyId, userDetails);
        Episode episode = Episode.builder().chapter(chapter).story(story).title("임시 제목").description("임시 내용").build();
        episodeRepository.save(episode);
        kafkaProducerService.sendCreate(episode);
        return episodeDtoCreateService.toEpisodeResponseDto(episode);
    }
}

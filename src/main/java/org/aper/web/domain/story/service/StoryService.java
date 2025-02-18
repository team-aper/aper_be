package org.aper.web.domain.story.service;

import com.aperlibrary.episode.entity.Episode;
import com.aperlibrary.story.entity.Story;
import com.aperlibrary.story.entity.constant.StoryGenreEnum;
import com.aperlibrary.story.entity.constant.StoryLineStyleEnum;
import com.aperlibrary.story.entity.constant.StoryRoutineEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.episode.dto.EpisodeResponseDto.CreatedEpisodeDto;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.episode.service.EpisodeMapper;
import org.aper.web.domain.kafka.service.KafkaEpisodesProducerService;
import org.aper.web.domain.paragraph.repository.ParagraphRepository;
import org.aper.web.domain.story.dto.StoryRequestDto;
import org.aper.web.domain.story.dto.StoryRequestDto.StoryCreateDto;
import org.aper.web.domain.story.dto.StoryResponseDto.CreatedStoryDto;
import org.aper.web.domain.story.dto.StoryResponseDto.GetStoryDto;
import org.aper.web.domain.story.repository.StoryRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.aper.web.global.util.EnumUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoryService {
    private final StoryRepository storyRepository;
    private final EpisodeMapper episodeMapper;
    private final StoryHelper storyHelper;
    private final StoryMapper storyMapper;
    private final EpisodeRepository episodeRepository;
    private final ParagraphRepository paragraphRepository;
    private final KafkaEpisodesProducerService producerService;
    private final EnumUtil enumUtil;

    @Transactional
    public void changePublicStatus(Long storyId, UserDetailsImpl userDetails) {
        Story existStory = storyHelper.validateStoryOwnership(storyId, userDetails);
        existStory.updateOnDisplay();
        storyRepository.save(existStory);

        //에피소드가 없는 스토리의 경우 엘라스틱서치에 넣을 수는 있으나 데이터를 관리하기 어려워짐
//        List<Episode> episodeList = existStory.getEpisodeList();
//        if (episodeList.isEmpty()) {
//            producerService.sendUpdateOnlyStory(existStory);
//            return;
//        }
        existStory.getEpisodeList().forEach(producerService::sendUpdate);
    }

    @Transactional
    public CreatedStoryDto createStory(UserDetailsImpl userDetails, StoryCreateDto storyCreateDto) {
        StoryRoutineEnum routineEnum = enumUtil.fromString(StoryRoutineEnum.class, storyCreateDto.routine());

        Story story = Story.builder()
                .title(storyCreateDto.title())
                .routine(routineEnum)
                .genre(enumUtil.fromString(StoryGenreEnum.class, storyCreateDto.genre()))
                .lineStyle(enumUtil.fromString(StoryLineStyleEnum.class, storyCreateDto.lineStyle()))
                .user(userDetails.user())
                .build();

        List<Episode> episodes = episodeMapper.createEpisodeList(routineEnum, story);
        story.addEpisodes(episodes);

        storyRepository.save(story);

        Episode kafkaEpisode = Episode.builder().story(story).build();
        producerService.sendCreate(kafkaEpisode);

        return new CreatedStoryDto(story.getId());
    }

    @Transactional(readOnly = true)
    public GetStoryDto getStory(UserDetailsImpl userDetails, Long storyId) {
        Story story = storyHelper.validateStoryAccessibility(storyId);

        if (storyHelper.isOwnStory(storyId, userDetails)) {
            return storyMapper.createGetStoryDtoWithEpisodes(story);
        }

        if (!story.isOnDisplay()) {
            throw new ServiceException(ErrorCode.STORY_NOT_PUBLISHED);
        }

        return storyMapper.createGetStoryDtoWithPublishedEpisodes(story);
    }

    @Transactional
    public void changeCover(UserDetailsImpl userDetails, Long storyId, StoryRequestDto.CoverChangeDto coverChangeDto) {
        Story story =  storyHelper.validateStoryOwnership(storyId, userDetails);
        story.updateCover(
                coverChangeDto.title(),
                enumUtil.fromString(StoryGenreEnum.class, coverChangeDto.genre()),
                enumUtil.fromString(StoryLineStyleEnum.class, coverChangeDto.lineStyle())
        );
        storyRepository.save(story);
    }

    @Transactional
    public void deleteStory(UserDetailsImpl userDetails, Long storyId) {
        Story story = storyHelper.validateStoryOwnership(storyId, userDetails);

        List<Long> episodeIds = episodeRepository.findEpisodeIdsByStoryId(storyId);

        if (!episodeIds.isEmpty()) {
            paragraphRepository.deleteByEpisodeIds(episodeIds);
            episodeRepository.deleteByStoryId(storyId);
        }

        storyRepository.deleteById(story.getId());
    }


    public CreatedEpisodeDto createEpisode(UserDetailsImpl userDetails, Long storyId, Long chapter) {
        Story story = storyHelper.validateStoryOwnership(storyId, userDetails);
        Episode episode = Episode.builder().chapter(chapter).story(story).build();
        episodeRepository.save(episode);
        producerService.sendCreate(episode);
        return episodeMapper.toEpisodeResponseDto(episode);
    }
}

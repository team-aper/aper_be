package org.aper.web.domain.episode.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.story.constant.StoryRoutineEnum;
import org.aper.web.domain.story.dto.StoryResponseDto.EpisodeResponseDto;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.user.entity.User;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository episodeRepository;

    @Transactional(readOnly = true)
    public List<EpisodeResponseDto> getEpisodesWithDDay(Long storyId) {
        List<Episode> episodes = episodeRepository.findAllByStoryId(storyId);

        if (episodes.isEmpty()) {
            return Collections.emptyList();
        }

        return episodes.stream()
                .map(this::toEpisodeResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EpisodeResponseDto> getPublishedEpisodesWithDDay(Long userId) {
        List<Episode> episodes = episodeRepository.findAllByEpisodeOnlyPublished(userId);

        if (episodes.isEmpty()){
            return Collections.emptyList();
        }

        return episodes.stream()
                .map(this::toEpisodeResponseDto)
                .collect(Collectors.toList());
    }

    private EpisodeResponseDto toEpisodeResponseDto(Episode episode) {
        StoryRoutineEnum routine = episode.getStory().getRoutine();
        int dDay = routine.calculateEpisodeDDay(episode.getCreatedAt(), episode.getChapter().intValue());
        String dDayString = dDay >= 0 ? "D-" + dDay : "D+" + Math.abs(dDay);

        return new EpisodeResponseDto(
                episode.getTitle(),
                episode.getChapter(),
                episode.getDescription(),
                episode.getCreatedAt(),
                episode.getPublicDate(),
                episode.isOnDisplay(),
                dDayString
        );
    }

    public List<Episode> createEpisodeList(StoryRoutineEnum routineEnum, Story story) {
        return routineEnum.createEpisodes(story);
    }

    @Transactional
    public void changePublicStatus(Long episodeId, UserDetailsImpl userDetails) {
        Episode existEpisode = episodeRepository.findByEpisodeAuthor(episodeId).orElseThrow(() ->
                new ServiceException(ErrorCode.EPISODE_NOT_FOUND)
        );

        User episodeAuthor = existEpisode.getStory().getUser();
        User accessUser = userDetails.user();

        if (!episodeAuthor.getUserId().equals(accessUser.getUserId())) {
            throw new ServiceException(ErrorCode.NOT_AUTHOR_OF_EPISODE);
        }

        existEpisode.updateOnDisplay();
        episodeRepository.save(existEpisode);
    }
}

package org.aper.web.domain.episode.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.dto.EpisodeResponseDto;
import org.aper.web.domain.episode.dto.EpisodeResponseDto.CreatedEpisodeDto;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.story.entity.constant.StoryRoutineEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EpisodeDtoCreateService {

    private final EpisodeRepository episodeRepository;

    @Transactional(readOnly = true)
    public List<CreatedEpisodeDto> getEpisodesWithDDay(Long storyId) {
        List<Episode> episodes = episodeRepository.findAllByStoryId(storyId);

        if (episodes.isEmpty()) {
            return Collections.emptyList();
        }

        return episodes.stream()
                .map(this::toEpisodeResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EpisodeResponseDto.CreatedEpisodeDto> getPublishedEpisodesWithDDay(Long userId) {
        List<Episode> episodes = episodeRepository.findAllByEpisodeOnlyPublished(userId);

        if (episodes.isEmpty()){
            return Collections.emptyList();
        }

        return episodes.stream()
                .map(this::toEpisodeResponseDto)
                .collect(Collectors.toList());
    }

    public CreatedEpisodeDto toEpisodeResponseDto(Episode episode) {
        StoryRoutineEnum routine = episode.getStory().getRoutine();
        int dDay = routine.calculateEpisodeDDay(episode.getCreatedAt(), episode.getChapter().intValue());
        String dDayString = dDay >= 0 ? "D-" + dDay : "D+" + Math.abs(dDay);

        String truncatedDescription = truncateDescription(episode.getDescription());

        return new EpisodeResponseDto.CreatedEpisodeDto(
                episode.getTitle(),
                episode.getChapter(),
                truncatedDescription,
                episode.getCreatedAt(),
                episode.getPublicDate(),
                episode.isOnDisplay(),
                dDayString
        );
    }

    private String truncateDescription(String description) {
        if (description == null) {
            return null;
        }

        return description.length() > 250 ? description.substring(0, 250) + "..." : description;
    }

    public List<Episode> createEpisodeList(StoryRoutineEnum routineEnum, Story story) {
        return routineEnum.createEpisodes(story);
    }
}

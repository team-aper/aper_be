package org.aper.web.domain.episode.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.dto.EpisodeResponseDto;
import org.aper.web.domain.episode.dto.EpisodeResponseDto.CreatedEpisodeDto;
import org.aper.web.domain.episode.dto.EpisodeResponseDto.EpisodeHeaderDto;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.story.entity.constant.StoryRoutineEnum;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EpisodeMapper {

    private final EpisodeRepository episodeRepository;
    private final DDayService dDayService;

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
    public List<CreatedEpisodeDto> getPublishedEpisodesWithDDay(Long userId) {
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
        int dDay = routine.calculateEpisodeDDay(episode.getCreatedAt(), episode.getChapter().intValue(), dDayService);
        String dDayString = dDay >= 0 ? "D-" + dDay : "D+" + Math.abs(dDay);
        LocalDateTime date = episode.isOnDisplay() ? episode.getPublicDate() : episode.getCreatedAt();
        return new EpisodeResponseDto.CreatedEpisodeDto(
                episode.getId(),
                episode.getTitle(),
                episode.getChapter(),
                date,
                episode.getDescription(),
                episode.isOnDisplay(),
                dDayString
        );
    }

    public EpisodeHeaderDto toEpisodeHeaderDto(Episode episode){

        LocalDateTime date = episode.isOnDisplay() ? episode.getPublicDate() : episode.getCreatedAt();

        return new EpisodeHeaderDto(
                episode.getId(),
                episode.getStory().getUser().getUserId(),
                episode.getStory().getId(),
                episode.getTitle(),
                episode.getStory().getTitle(),
                episode.getChapter(),
                episode.getStory().getGenre().name(),
                date,
                episode.isOnDisplay()
        );
    }

    public List<Episode> createEpisodeList(StoryRoutineEnum routineEnum, Story story) {
        return routineEnum.createEpisodes(story);
    }
}

package org.aper.web.domain.episode.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.dto.EpisodeRequestDto.DeleteEpisodeDto;
import org.aper.web.domain.episode.dto.EpisodeRequestDto.TextChangeDto;
import org.aper.web.domain.episode.dto.EpisodeRequestDto.TitleChangeDto;
import org.aper.web.domain.episode.dto.EpisodeResponseDto.EpisodeHeaderDto;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.story.service.StoryValidationService;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final EpisodeValidationService episodeValidationService;
    private final EpisodeDtoCreateService episodeDtoCreateService;
    private final StoryValidationService storyValidationService;

    @Transactional
    public void changePublicStatus(Long episodeId, UserDetailsImpl userDetails) {
        Episode episode = episodeValidationService.validateEpisodeExists(episodeId);
        episodeValidationService.validateUserIsAuthor(episode, userDetails);

        episode.updateOnDisplay();
        episodeRepository.save(episode);
    }

    @Transactional
    public void changeTitle(UserDetailsImpl userDetails, Long episodeId, TitleChangeDto titleChangeDto) {
        Episode episode = episodeValidationService.validateEpisodeExists(episodeId);
        episodeValidationService.validateUserIsAuthor(episode, userDetails);

        episode.updateTitle(titleChangeDto.title());
        episodeRepository.save(episode);
    }

    @Transactional
    public void changeText(UserDetailsImpl userDetails, Long episodeId, TextChangeDto textChangeDto) {
        Episode episode = episodeValidationService.validateEpisodeExists(episodeId);
        episodeValidationService.validateUserIsAuthor(episode, userDetails);

        episode.updateText(textChangeDto.text());
        episodeRepository.save(episode);
    }

    @Transactional
    public void deleteEpisode(UserDetailsImpl userDetails, Long episodeId, DeleteEpisodeDto episodeDto) {
        Episode episode = episodeValidationService.validateEpisodeExists(episodeId);
        episodeValidationService.validateUserIsAuthor(episode, userDetails);
        episodeRepository.delete(episode);
        episodeRepository.flush();
        episodeRepository.decrementChaptersAfterDeletion(episodeDto.storyId(), episodeDto.chapter());
    }

    public EpisodeHeaderDto getEpisodeHeader(UserDetailsImpl userDetails, Long episodeId) {
        Episode episode = episodeValidationService.validateEpisodeExists(episodeId);
        if (storyValidationService.isOwnStory(episode.getStory().getId(), userDetails)){
            return episodeDtoCreateService.toEpisodeHeaderDto(episode);
        }

        if (!episode.isOnDisplay()){
            throw new ServiceException(ErrorCode.EPISODE_NOT_PUBLISHED);
        }

        return episodeDtoCreateService.toEpisodeHeaderDto(episode);
    }

}

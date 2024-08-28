package org.aper.web.domain.episode.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.dto.EpisodeRequestDto.TextChangeDto;
import org.aper.web.domain.episode.dto.EpisodeRequestDto.TitleChangeDto;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final EpisodeValidationService episodeValidationService;

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
}

package org.aper.web.domain.episode.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.user.entity.User;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository episodeRepository;

    public void changePublicStatus(Long episodeId, UserDetailsImpl userDetails) {
        Episode existEpisode = episodeRepository.findByEpisodeAuthor(episodeId).orElseThrow(() ->
            new ServiceException(ErrorCode.EPISODE_NOT_FOUND)
        );

        User episodeAuthor = existEpisode.getStory().getUser();
        User accessUser = userDetails.user();

        if(!episodeAuthor.getUserId().equals(accessUser.getUserId())) {
            throw new ServiceException(ErrorCode.NOT_AUTHOR_OF_EPISODE);
        }

        existEpisode.updateOnDisplay();
    }
}

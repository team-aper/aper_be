package org.aper.web.domain.episode.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.user.entity.User;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EpisodeHelper {
    private final EpisodeRepository episodeRepository;

    public Episode validateEpisodeExists(Long episodeId) {
        return episodeRepository.findByEpisodeAuthor(episodeId)
                .orElseThrow(() -> new ServiceException(ErrorCode.EPISODE_NOT_FOUND));
    }

    public void validateUserIsAuthor(Episode episode, UserDetailsImpl userDetails) {
        User episodeAuthor = episode.getStory().getUser();
        User accessUser = userDetails.user();

        if (!episodeAuthor.getUserId().equals(accessUser.getUserId())) {
            throw new ServiceException(ErrorCode.NOT_AUTHOR_OF_EPISODE);
        }
    }

}

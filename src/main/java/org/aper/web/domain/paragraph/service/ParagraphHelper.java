package org.aper.web.domain.paragraph.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.paragraph.entity.Paragraph;
import org.aper.web.domain.paragraph.repository.ParagraphRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParagraphHelper {

    private final EpisodeRepository episodeRepository;
    private final ParagraphRepository paragraphRepository;

    public Episode validateEpisodeOwnership(Long episodeId, UserDetailsImpl userDetails) {
        Episode episode = episodeRepository.findById(episodeId).orElseThrow(() ->
                new ServiceException(ErrorCode.EPISODE_NOT_FOUND));

        if (!episode.getStory().getUser().getUserId().equals(userDetails.user().getUserId())) {
            throw new ServiceException(ErrorCode.NOT_AUTHOR_OF_EPISODE);
        }

        return episode;
    }

    public Episode validateEpisodeExists(Long episodeId) {
        return episodeRepository.findById(episodeId).orElseThrow(() ->
                new ServiceException(ErrorCode.EPISODE_NOT_FOUND));
    }

    public Paragraph validateParagraphExists(String uuid) {
        return paragraphRepository.findByUuid(uuid).orElseThrow(() ->
                new ServiceException(ErrorCode.PARAGRAPH_NOT_FOUND));
    }

    public Long extractEpisodeIdFromUrl(String url) {
        String[] parts = url.split("/");
        return Long.parseLong(parts[2]);
    }

}

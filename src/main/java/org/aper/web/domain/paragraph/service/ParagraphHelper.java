package org.aper.web.domain.paragraph.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.paragraph.entity.Paragraph;
import org.aper.web.domain.paragraph.repository.ParagraphRepository;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParagraphHelper {

    private final ParagraphRepository paragraphRepository;
    private final EpisodeRepository episodeRepository;

    // 문단 존재 및 소유권 확인
    public Paragraph validateParagraphOwnership(String paragraphId, UserDetailsImpl userDetails) {
        Paragraph paragraph = paragraphRepository.findById(paragraphId).orElseThrow(() ->
                new ServiceException(ErrorCode.PARAGRAPH_NOT_FOUND));

        if (!paragraph.getEpisode().getStory().getUser().getUserId().equals(userDetails.user().getUserId())) {
            throw new ServiceException(ErrorCode.NOT_AUTHOR_OF_EPISODE);
        }

        return paragraph;
    }

    // 새로운 문단의 에피소드 유효성 검증
    public Episode validateEpisodeExists(Long episodeId) {
        return episodeRepository.findById(episodeId).orElseThrow(() ->
                new ServiceException(ErrorCode.EPISODE_NOT_FOUND));
    }

    // 문단 존재 여부 확인
    public Paragraph validateParagraphExists(String uuid) {
        return paragraphRepository.findByUuid(uuid).orElseThrow(() ->
                new ServiceException(ErrorCode.PARAGRAPH_NOT_FOUND));
    }
}

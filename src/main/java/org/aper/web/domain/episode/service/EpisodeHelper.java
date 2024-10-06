package org.aper.web.domain.episode.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.paragraph.dto.ParagraphResponseDto.Paragraphs;
import org.aper.web.domain.paragraph.entity.Paragraph;
import org.aper.web.domain.user.entity.User;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
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

    public List<Paragraphs> getSortedParagraphs(List<Paragraph> paragraphs) {

        Paragraph firstParagraph = paragraphs.stream()
                .filter(p -> p.getPreviousUuid() == null)
                .findFirst()
                .orElseThrow(() -> new ServiceException(ErrorCode.PARAGRAPH_NOT_FOUND));

        List<Paragraphs> sortedParagraphs = new ArrayList<>();
        Paragraph current = firstParagraph;

        while (current != null) {
            sortedParagraphs.add(new Paragraphs(
                    current.getUuid(),
                    current.getContent(),
                    current.getPreviousUuid(),
                    current.getNextUuid()
            ));

            String nextUuid = current.getNextUuid();
            current = paragraphs.stream()
                    .filter(p -> nextUuid != null && nextUuid.equals(p.getUuid()))
                    .findFirst()
                    .orElse(null);
        }

        return sortedParagraphs;
    }

}

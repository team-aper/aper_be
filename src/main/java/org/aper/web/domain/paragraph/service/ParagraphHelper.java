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

import java.util.List;

@Component
@RequiredArgsConstructor
public class ParagraphHelper {

    private final EpisodeRepository episodeRepository;
    private final ParagraphRepository paragraphRepository;

    public Episode validateEpisode(Long episodeId, UserDetailsImpl userDetails, boolean checkOwnership) {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new ServiceException(ErrorCode.EPISODE_NOT_FOUND));

        if (checkOwnership && !episode.getStory().getUser().getUserId().equals(userDetails.user().getUserId())) {
            throw new ServiceException(ErrorCode.NOT_AUTHOR_OF_EPISODE);
        }

        return episode;
    }

    public Episode validateEpisodeOwnership(Long episodeId, UserDetailsImpl userDetails) {
        return validateEpisode(episodeId, userDetails, true);
    }

    public Paragraph validateParagraphExists(String uuid) {
        return paragraphRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(ErrorCode.PARAGRAPH_NOT_FOUND));
    }

    public Long extractEpisodeIdFromUrl(String url) {
        try {
            return Long.parseLong(url.split("/")[2]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new ServiceException(ErrorCode.INVALID_BATCH_URL);
        }
    }

    public String truncateParagraph(String paragraph) {
        if (paragraph == null) {
            return null;
        }
        return paragraph.length() > 250 ? paragraph.substring(0, 250) + "..." : paragraph + "...";
    }

    public void updateEpisodeDescription(Episode episode) {
        List<Paragraph> paragraphs = episode.getParagraphs();

        Paragraph firstParagraph = paragraphs.stream()
                .filter(paragraph -> paragraph.getPreviousUuid() == null)
                .findFirst()
                .orElseThrow(() -> new ServiceException(ErrorCode.PARAGRAPH_NOT_FOUND));

        String truncatedParagraph = truncateParagraph(firstParagraph.getContent());
        episode.updateDescription(truncatedParagraph);
        episodeRepository.save(episode);
    }

    public void updatePreviousParagraph(String previousUuid, String nextUuid, List<Paragraph> paragraphsToUpdate) {
        if (previousUuid != null) {
            Paragraph previousParagraph = paragraphRepository.findByUuid(previousUuid)
                    .orElseThrow(() -> new ServiceException(ErrorCode.PARAGRAPH_NOT_FOUND));
            previousParagraph.updateNextUuid(nextUuid);
            paragraphsToUpdate.add(previousParagraph);
        }
    }

    public void updateNextParagraph(String previousUuid, String nextUuid, List<Paragraph> paragraphsToUpdate) {
        if (nextUuid != null) {
            Paragraph nextParagraph = paragraphRepository.findByUuid(nextUuid)
                    .orElseThrow(() -> new ServiceException(ErrorCode.PARAGRAPH_NOT_FOUND));
            nextParagraph.updatePreviousUuid(previousUuid);
            paragraphsToUpdate.add(nextParagraph);
        }
    }
}

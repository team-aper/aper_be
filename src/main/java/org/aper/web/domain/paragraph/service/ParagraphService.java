package org.aper.web.domain.paragraph.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.paragraph.dto.ParagraphRequestDto.ParagraphDto;
import org.aper.web.domain.paragraph.entity.Paragraph;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.paragraph.repository.ParagraphRepository;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParagraphService {

    private final ParagraphRepository paragraphRepository;
    private final ParagraphHelper paragraphHelper;

    @Transactional
    public void processBatch(Long episodeId, List<ParagraphDto> modified, List<ParagraphDto> added, List<String> deleted, UserDetailsImpl userDetails) {
        handleModifiedParagraphs(modified, userDetails);
        handleAddedParagraphs(episodeId, added);
        handleDeletedParagraphs(deleted);
    }

    private void handleModifiedParagraphs(List<ParagraphDto> modified, UserDetailsImpl userDetails) {
        for (ParagraphDto dto : modified) {
            Paragraph paragraph = paragraphHelper.validateParagraphOwnership(dto.uuid(), userDetails);

            // 필드별로 값이 다를 때만 업데이트
            if (!paragraph.getContent().equals(dto.content())) {
                paragraph.updateContent(dto.content());
            }
            if (dto.previousUuid() != null && !dto.previousUuid().equals(paragraph.getPreviousUuid())) {
                paragraph.updatePreviousUuid(dto.previousUuid());
            }
            if (dto.nextUuid() != null && !dto.nextUuid().equals(paragraph.getNextUuid())) {
                paragraph.updateNextUuid(dto.nextUuid());
            }

            paragraphRepository.save(paragraph);
        }
    }

    private void handleAddedParagraphs(Long episodeId, List<ParagraphDto> added) {
        for (ParagraphDto dto : added) {
            Episode episode = paragraphHelper.validateEpisodeExists(episodeId);

            Paragraph newParagraph = Paragraph.builder()
                    .uuid(dto.uuid())
                    .content(dto.content())
                    .previousUuid(dto.previousUuid())
                    .nextUuid(dto.nextUuid())
                    .episode(episode)
                    .build();

            paragraphRepository.save(newParagraph);
        }
    }

    private void handleDeletedParagraphs(List<String> deleted) {
        for (String paragraphId : deleted) {
            Paragraph paragraph = paragraphHelper.validateParagraphExists(paragraphId);
            paragraphRepository.delete(paragraph);
        }
    }
}

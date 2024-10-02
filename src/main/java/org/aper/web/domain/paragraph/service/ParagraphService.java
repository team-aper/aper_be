package org.aper.web.domain.paragraph.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.paragraph.dto.ParagraphRequestDto.BatchOperation;
import org.aper.web.domain.paragraph.dto.ParagraphRequestDto.BatchRequest;
import org.aper.web.domain.paragraph.dto.ParagraphRequestDto.ItemPayload;
import org.aper.web.domain.paragraph.entity.Paragraph;
import org.aper.web.domain.paragraph.repository.ParagraphRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParagraphService {

    private final ParagraphRepository paragraphRepository;
    private final ParagraphHelper paragraphHelper;

    @Transactional
    public void processBatch(BatchRequest request, UserDetailsImpl userDetails) {
        List<BatchOperation> operations = request.batch();
        Set<String> deletedUuids = new HashSet<>();
        Set<String> processedUuids = new HashSet<>();

        for (BatchOperation operation : operations) {
            Long episodeId = paragraphHelper.extractEpisodeIdFromUrl(operation.url());
            Episode episode = paragraphHelper.validateEpisodeOwnership(episodeId, userDetails);

            switch (operation.method()) {
                case "DELETE":
                    handleDeletedParagraph(operation.body(), deletedUuids);
                    break;
                case "PUT":
                    handleModifiedParagraph(operation.body(), deletedUuids, processedUuids);
                    break;
                case "POST":
                    handleAddedParagraph(episode, operation.body(), deletedUuids, processedUuids);
                    break;
                default:
                    throw new ServiceException(ErrorCode.INVALID_BATCH_REQUEST);
            }
        }
    }

    private void handleDeletedParagraph(List<ItemPayload> itemPayloads, Set<String> deletedUuids) {
        for (ItemPayload itemPayload : itemPayloads) {
            Paragraph paragraph = paragraphHelper.validateParagraphExists(itemPayload.id());
            paragraphRepository.delete(paragraph);
            deletedUuids.add(itemPayload.id());
        }
    }

    private void handleModifiedParagraph(List<ItemPayload> itemPayloads, Set<String> deletedUuids, Set<String> processedUuids) {
        List<Paragraph> paragraphsToSave = new ArrayList<>();

        for (ItemPayload itemPayload : itemPayloads) {

            if (deletedUuids.contains(itemPayload.id())) {
                continue;
            }

            if (processedUuids.contains(itemPayload.id())) {
                continue;
            }

            Paragraph paragraph = paragraphHelper.validateParagraphExists(itemPayload.id());

            if (!paragraph.getContent().equals(itemPayload.content())) {
                paragraph.updateContent(itemPayload.content());
            }
            if (itemPayload.prev() != null && !itemPayload.prev().equals(paragraph.getPreviousUuid())) {
                paragraph.updatePreviousUuid(itemPayload.prev());
            }
            if (itemPayload.next() != null && !itemPayload.next().equals(paragraph.getNextUuid())) {
                paragraph.updateNextUuid(itemPayload.next());
            }

            paragraphsToSave.add(paragraph);
            processedUuids.add(itemPayload.id());
        }

        paragraphRepository.saveAll(paragraphsToSave);
    }

    private void handleAddedParagraph(Episode episode, List<ItemPayload> itemPayloads, Set<String> deletedUuids, Set<String> processedUuids) {
        List<Paragraph> paragraphsToAdd = new ArrayList<>();

        for (ItemPayload itemPayload : itemPayloads) {

            if (deletedUuids.contains(itemPayload.id())) {
                continue;
            }

            if (processedUuids.contains(itemPayload.id())) {
                continue;
            }

            if (paragraphRepository.findByUuid(itemPayload.id()).isPresent()) {
                log.error("Paragraph with UUID {} already exists", itemPayload.id());
                throw new ServiceException(ErrorCode.PARAGRAPH_ALREADY_EXISTS);
            }

            Paragraph newParagraph = Paragraph.builder()
                    .uuid(itemPayload.id())
                    .content(itemPayload.content())
                    .previousUuid(itemPayload.prev())
                    .nextUuid(itemPayload.next())
                    .episode(episode)
                    .build();

            paragraphsToAdd.add(newParagraph);
            processedUuids.add(itemPayload.id());
        }

        paragraphRepository.saveAll(paragraphsToAdd);
    }
}

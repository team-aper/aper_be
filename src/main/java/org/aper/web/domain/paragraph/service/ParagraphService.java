package org.aper.web.domain.paragraph.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParagraphService {

    private final ParagraphRepository paragraphRepository;
    private final ParagraphHelper paragraphHelper;
    private final EpisodeRepository episodeRepository;

    @Transactional
    public void processBatch(BatchRequest request, UserDetailsImpl userDetails) {
        List<BatchOperation> operations = request.batch();
        Set<String> deletedUuids = new HashSet<>();
        Set<String> processedUuids = new HashSet<>();
        boolean firstParagraphUpdated = false;

        for (BatchOperation operation : operations) {
            Long episodeId = paragraphHelper.extractEpisodeIdFromUrl(operation.url());
            Episode episode = paragraphHelper.validateEpisodeOwnership(episodeId, userDetails);

            switch (operation.method()) {
                case "DELETE":
                    handleDeletedParagraph(operation.body(), deletedUuids, episode.getParagraphs());
                    break;
                case "PUT":
                    if (handleModifiedParagraph(operation.body(), deletedUuids, processedUuids)) {
                        firstParagraphUpdated = true;
                    }
                    break;
                case "POST":
                    if (handleAddedParagraph(episode, operation.body(), deletedUuids, processedUuids)) {
                        firstParagraphUpdated = true;
                    }
                    break;
                default:
                    throw new ServiceException(ErrorCode.INVALID_BATCH_REQUEST);
            }

            if (firstParagraphUpdated) {
                updateEpisodeDescription(episode);
            }
        }
    }

    private void handleDeletedParagraph(List<ItemPayload> itemPayloads, Set<String> deletedUuids, List<Paragraph> allParagraphs) {
        Map<String, Paragraph> paragraphMap = allParagraphs.stream()
                .collect(Collectors.toMap(Paragraph::getUuid, p -> p));

        List<Paragraph> paragraphsToUpdate = new ArrayList<>();

        for (ItemPayload itemPayload : itemPayloads) {
            Paragraph paragraphToDelete = paragraphMap.get(itemPayload.id());

            if (paragraphToDelete == null) {
                throw new ServiceException(ErrorCode.PARAGRAPH_NOT_FOUND);
            }

            String previousUuid = paragraphToDelete.getPreviousUuid();
            String nextUuid = paragraphToDelete.getNextUuid();

            if (previousUuid != null) {
                Paragraph previousParagraph = paragraphMap.get(previousUuid);
                if (previousParagraph != null) {
                    previousParagraph.updateNextUuid(nextUuid);
                    paragraphsToUpdate.add(previousParagraph);
                }
            }

            if (nextUuid != null) {
                Paragraph nextParagraph = paragraphMap.get(nextUuid);
                if (nextParagraph != null) {
                    nextParagraph.updatePreviousUuid(previousUuid);
                    paragraphsToUpdate.add(nextParagraph);
                }
            }

            paragraphRepository.delete(paragraphToDelete);
            deletedUuids.add(itemPayload.id());
        }

        paragraphRepository.saveAll(paragraphsToUpdate);
        paragraphRepository.flush();
    }



    private boolean handleModifiedParagraph(List<ItemPayload> itemPayloads, Set<String> deletedUuids, Set<String> processedUuids) {
        List<Paragraph> paragraphsToSave = new ArrayList<>();
        boolean firstParagraphUpdated = false;

        for (ItemPayload itemPayload : itemPayloads) {
            if (deletedUuids.contains(itemPayload.id()) || processedUuids.contains(itemPayload.id())) {
                continue;
            }

            Paragraph paragraph = paragraphHelper.validateParagraphExists(itemPayload.id());

            if (paragraphHelper.isFirstParagraph(itemPayload.prev()) && !paragraph.getContent().equals(itemPayload.content())) {
                firstParagraphUpdated = true;
            }

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
        paragraphRepository.flush();

        return firstParagraphUpdated;
    }

    private boolean handleAddedParagraph(Episode episode, List<ItemPayload> itemPayloads, Set<String> deletedUuids, Set<String> processedUuids) {
        List<Paragraph> paragraphsToAdd = new ArrayList<>();
        boolean firstParagraphAdded = false;

        for (ItemPayload itemPayload : itemPayloads) {
            if (deletedUuids.contains(itemPayload.id()) || processedUuids.contains(itemPayload.id())) {
                continue;
            }

            if (paragraphRepository.findByUuid(itemPayload.id()).isPresent()) {
                throw new ServiceException(ErrorCode.PARAGRAPH_ALREADY_EXISTS);
            }

            Paragraph newParagraph = Paragraph.builder()
                    .uuid(itemPayload.id())
                    .content(itemPayload.content())
                    .previousUuid(itemPayload.prev())
                    .nextUuid(itemPayload.next())
                    .episode(episode)
                    .build();

            if (paragraphHelper.isFirstParagraph(itemPayload.prev())) {
                firstParagraphAdded = true;
            }

            paragraphsToAdd.add(newParagraph);
            processedUuids.add(itemPayload.id());
        }

        paragraphRepository.saveAll(paragraphsToAdd);
        paragraphRepository.flush();
        return firstParagraphAdded;
    }

    private void updateEpisodeDescription(Episode episode) {

        List<Paragraph> paragraphs = episode.getParagraphs();

        Paragraph firstParagraph = paragraphs.stream()
                .filter(paragraph -> paragraph.getPreviousUuid() == null)
                .findFirst()
                .orElseThrow(() -> new ServiceException(ErrorCode.PARAGRAPH_NOT_FOUND));

        String truncatedParagraph = paragraphHelper.truncateParagraph(firstParagraph.getContent());

        episode.updateDescription(truncatedParagraph);
        episodeRepository.save(episode);
    }
}

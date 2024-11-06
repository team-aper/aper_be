package org.aper.web.domain.paragraph.service.method;

import org.aper.web.global.properties.BatchProperties;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.paragraph.dto.ParagraphRequestDto.ItemPayload;
import org.aper.web.domain.paragraph.entity.Paragraph;
import org.aper.web.domain.paragraph.repository.ParagraphRepository;
import org.aper.web.domain.paragraph.service.ParagraphHelper;
import org.aper.web.global.batch.service.method.BatchPostService;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParagraphPostService implements BatchPostService<ItemPayload> {

    private final ParagraphRepository paragraphRepository;
    private final ParagraphHelper paragraphHelper;
    private final EpisodeRepository episodeRepository;
    private final BatchProperties batchProperties;
    private final EntityManager entityManager;

    @Override
    public boolean handleAddedOperation(List<ItemPayload> itemPayloads, Set<String> deletedUuids, Long episodeId) {
        List<Paragraph> paragraphsToAdd = new ArrayList<>();
        List<Paragraph> paragraphsToUpdate = new ArrayList<>();
        boolean firstParagraphAdded = false;
        Episode episode = episodeRepository.getReferenceById(episodeId);
        int batchSize = batchProperties.getBatchSize();

        for (int i = 0; i < itemPayloads.size(); i++) {
            ItemPayload itemPayload = itemPayloads.get(i);

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

            paragraphsToAdd.add(newParagraph);

            if (itemPayload.prev() == null) {
                firstParagraphAdded = true;
            }

            if ((i + 1) % batchSize == 0) {
                paragraphRepository.saveAll(paragraphsToAdd);
                entityManager.flush();
                entityManager.clear();
                paragraphsToAdd.clear();
            }
        }

        if (!paragraphsToAdd.isEmpty()) {
            paragraphRepository.saveAll(paragraphsToAdd);
            entityManager.flush();
            entityManager.clear();
        }

        log.info("Created and updated paragraphs: {}", paragraphsToAdd.stream().map(Paragraph::getUuid).collect(Collectors.toList()));
        return firstParagraphAdded;
    }
}

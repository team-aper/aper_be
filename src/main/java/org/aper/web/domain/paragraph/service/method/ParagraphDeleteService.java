package org.aper.web.domain.paragraph.service.method;

import org.aper.web.global.properties.BatchProperties;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.paragraph.dto.ParagraphRequestDto.ItemPayload;
import org.aper.web.domain.paragraph.entity.Paragraph;
import org.aper.web.domain.paragraph.repository.ParagraphRepository;
import org.aper.web.domain.paragraph.service.ParagraphHelper;
import org.aper.web.global.batch.service.method.BatchDeleteService;
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
public class ParagraphDeleteService implements BatchDeleteService<ItemPayload> {

    private final ParagraphRepository paragraphRepository;
    private final ParagraphHelper paragraphHelper;
    private final BatchProperties batchProperties;
    private final EntityManager entityManager;

    @Override
    public void handleDeletedOperation(List<ItemPayload> itemPayloads, Set<String> deletedUuids, Long episodeId) {

        List<Paragraph> paragraphsToUpdate = new ArrayList<>();
        int batchSize = batchProperties.getBatchSize();

        for (int i = 0; i < itemPayloads.size(); i++) {
            ItemPayload itemPayload = itemPayloads.get(i);
            Paragraph paragraphToDelete = paragraphRepository.findByUuid(itemPayload.id())
                    .orElseThrow(() -> new ServiceException(ErrorCode.PARAGRAPH_NOT_FOUND));

            String previousUuid = paragraphToDelete.getPreviousUuid();
            String nextUuid = paragraphToDelete.getNextUuid();

            paragraphHelper.updatePreviousParagraph(previousUuid, nextUuid, paragraphsToUpdate);
            paragraphHelper.updateNextParagraph(previousUuid, nextUuid, paragraphsToUpdate);

            paragraphRepository.delete(paragraphToDelete);
            deletedUuids.add(itemPayload.id());
            log.info("Deleted paragraph with id: {}", itemPayload.id());

            if ((i + 1) % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
                paragraphsToUpdate.clear();
            }
        }

        entityManager.flush();
        entityManager.clear();

        if (!paragraphsToUpdate.isEmpty()) {
            paragraphRepository.saveAll(paragraphsToUpdate);
            entityManager.flush();
            entityManager.clear();
            log.info("Updated paragraphs after deletion: {}", paragraphsToUpdate.stream().map(Paragraph::getUuid).collect(Collectors.toList()));
        }
    }
}

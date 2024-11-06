package org.aper.web.domain.paragraph.service.method;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.paragraph.dto.ParagraphRequestDto.ItemPayload;
import org.aper.web.domain.paragraph.entity.Paragraph;
import org.aper.web.domain.paragraph.repository.ParagraphRepository;
import org.aper.web.domain.paragraph.service.ParagraphHelper;
import org.aper.web.global.batch.service.method.BatchPutService;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.properties.BatchProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParagraphPutService implements BatchPutService<ItemPayload> {

    private final ParagraphRepository paragraphRepository;
    private final ParagraphHelper paragraphHelper;
    private final BatchProperties batchProperties;
    private final EntityManager entityManager;

    @Override
    public boolean handleModifiedOperation(List<ItemPayload> itemPayloads, Set<String> deletedUuids) {
        List<Paragraph> paragraphsToUpdate = new ArrayList<>();
        boolean firstParagraphUpdated = false;
        int batchSize = batchProperties.getBatchSize();

        for (int i = 0; i < itemPayloads.size(); i++) {
            ItemPayload itemPayload = itemPayloads.get(i);

            if (deletedUuids.contains(itemPayload.id())) {
                throw new ServiceException(ErrorCode.PARAGRAPH_ALREADY_DELETED);
            }

            Paragraph paragraph = paragraphHelper.validateParagraphExists(itemPayload.id());

            if (itemPayload.prev() == null) {
                firstParagraphUpdated = true;
            }

            paragraph.updateContent(itemPayload.content());
            paragraph.updatePreviousUuid(itemPayload.prev());
            paragraph.updateNextUuid(itemPayload.next());

            paragraphsToUpdate.add(paragraph);

            if ((i + 1) % batchSize == 0) {
                paragraphRepository.saveAll(paragraphsToUpdate);
                entityManager.flush();
                entityManager.clear();
                paragraphsToUpdate.clear();
            }
        }

        if (!paragraphsToUpdate.isEmpty()) {
            paragraphRepository.saveAll(paragraphsToUpdate);
            entityManager.flush();
            entityManager.clear();
        }

        log.info("Updated paragraphs: {}", paragraphsToUpdate.stream().map(Paragraph::getUuid).collect(Collectors.toList()));
        return firstParagraphUpdated;
    }
}

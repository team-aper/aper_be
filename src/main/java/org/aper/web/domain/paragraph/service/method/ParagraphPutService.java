package org.aper.web.domain.paragraph.service.method;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.paragraph.dto.ParagraphRequestDto.ItemPayload;
import org.aper.web.domain.paragraph.entity.Paragraph;
import org.aper.web.domain.paragraph.repository.ParagraphRepository;
import org.aper.web.domain.paragraph.service.ParagraphHelper;
import org.aper.web.global.batch.service.method.BatchPutService;
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
public class ParagraphPutService implements BatchPutService<ItemPayload> {

    private final ParagraphRepository paragraphRepository;
    private final ParagraphHelper paragraphHelper;

    @Override
    public boolean handleModifiedOperation(List<ItemPayload> itemPayloads, Set<String> deletedUuids) {
        List<Paragraph> paragraphsToUpdate = new ArrayList<>();
        boolean firstParagraphUpdated = false;

        for (ItemPayload itemPayload : itemPayloads) {

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
        }

        paragraphRepository.saveAll(paragraphsToUpdate);
        paragraphRepository.flush();
        log.info("Updated paragraphs: {}", paragraphsToUpdate.stream().map(Paragraph::getUuid).collect(Collectors.toList()));

        return firstParagraphUpdated;
    }
}
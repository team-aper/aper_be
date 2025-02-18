package org.aper.web.domain.paragraph.service.method;

import com.aperlibrary.episode.entity.Episode;
import com.aperlibrary.paragraph.entity.Paragraph;
import com.aperlibrary.paragraph.entity.TextAlignEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.paragraph.dto.ParagraphRequestDto.ItemPayload;
import org.aper.web.domain.paragraph.repository.ParagraphRepository;
import org.aper.web.domain.paragraph.service.ParagraphHelper;
import org.aper.web.global.batch.service.method.BatchPostService;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.util.EnumUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParagraphPostService implements BatchPostService<ItemPayload> {

    private final ParagraphRepository paragraphRepository;
    private final ParagraphHelper paragraphHelper;
    private final EnumUtil enumUtil;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean handleAddedOperation(List<ItemPayload> itemPayloads, Episode episode) {
        List<Paragraph> paragraphsToAdd = new ArrayList<>();
        List<Paragraph> paragraphsToUpdate = new ArrayList<>();
        boolean firstParagraphAdded = false;


        for (ItemPayload itemPayload : itemPayloads) {

            if (paragraphRepository.findByUuid(itemPayload.id()).isPresent()) {
                throw new ServiceException(ErrorCode.PARAGRAPH_ALREADY_EXISTS);
            }

            Paragraph newParagraph = Paragraph.builder()
                    .uuid(itemPayload.id())
                    .content(itemPayload.content())
                    .textAlign(enumUtil.fromString(TextAlignEnum.class, itemPayload.textAlign()))
                    .previousUuid(itemPayload.prev())
                    .nextUuid(itemPayload.next())
                    .episode(episode)
                    .build();

            paragraphsToAdd.add(newParagraph);

            if (itemPayload.prev() == null) {
                firstParagraphAdded = true;
            }
        }

        paragraphRepository.saveAll(paragraphsToAdd);
        entityManager.flush();

        for (ItemPayload itemPayload : itemPayloads) {
            paragraphHelper.updatePreviousParagraph(itemPayload.prev(), itemPayload.id(), paragraphsToUpdate);
        }

        paragraphRepository.saveAll(paragraphsToUpdate);
        entityManager.flush();
        entityManager.clear();

        log.info("Created and updated paragraphs: {}", paragraphsToAdd.stream().map(Paragraph::getUuid).collect(Collectors.toList()));
        return firstParagraphAdded;
    }
}
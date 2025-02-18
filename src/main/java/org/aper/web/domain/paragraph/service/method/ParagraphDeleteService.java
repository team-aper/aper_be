package org.aper.web.domain.paragraph.service.method;

import com.aperlibrary.paragraph.entity.Paragraph;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.paragraph.dto.ParagraphRequestDto.ItemPayload;
import org.aper.web.domain.paragraph.repository.ParagraphRepository;
import org.aper.web.domain.paragraph.service.ParagraphHelper;
import org.aper.web.global.batch.service.method.BatchDeleteService;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(rollbackFor = Exception.class) // 🔥 트랜잭션 추가
    public void handleDeletedOperation(List<ItemPayload> itemPayloads, Set<String> deletedUuids, Long episodeId) {
        List<Paragraph> paragraphsToUpdate = new ArrayList<>();
        List<Paragraph> paragraphsToDelete = new ArrayList<>();

        // 1️⃣ 삭제할 Paragraph 먼저 수집
        for (ItemPayload itemPayload : itemPayloads) {
            Paragraph paragraphToDelete = paragraphRepository.findByUuid(itemPayload.id())
                    .orElseThrow(() -> new ServiceException(ErrorCode.PARAGRAPH_NOT_FOUND));

            String previousUuid = paragraphToDelete.getPreviousUuid();
            String nextUuid = paragraphToDelete.getNextUuid();

            paragraphHelper.updatePreviousParagraph(previousUuid, nextUuid, paragraphsToUpdate);
            paragraphHelper.updateNextParagraph(previousUuid, nextUuid, paragraphsToUpdate);

            paragraphsToDelete.add(paragraphToDelete);
            deletedUuids.add(itemPayload.id());
        }

        // 2️⃣ 한번에 삭제 & 즉시 반영
        paragraphRepository.deleteAll(paragraphsToDelete);
        paragraphRepository.flush();

        log.info("Deleted paragraphs: {}", deletedUuids);

        // 3️⃣ 한번에 업데이트 & 즉시 반영
        paragraphRepository.saveAll(paragraphsToUpdate);
        paragraphRepository.flush();

        log.info("Updated paragraphs after deletion: {}", paragraphsToUpdate.stream().map(Paragraph::getUuid).collect(Collectors.toList()));
    }
}
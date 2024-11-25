package org.aper.web.domain.paragraph.service;

import com.aperlibrary.episode.entity.Episode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.paragraph.dto.ParagraphRequestDto.ItemPayload;
import org.aper.web.domain.paragraph.service.method.ParagraphDeleteService;
import org.aper.web.domain.paragraph.service.method.ParagraphPostService;
import org.aper.web.domain.paragraph.service.method.ParagraphPutService;
import org.aper.web.global.batch.dto.BatchRequestDto.BatchOperation;
import org.aper.web.global.batch.dto.BatchRequestDto.BatchRequest;
import org.aper.web.global.batch.service.BatchService;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParagraphService implements BatchService<ItemPayload> {

    private final ParagraphPutService paragraphPutService;
    private final ParagraphPostService paragraphPostService;
    private final ParagraphDeleteService paragraphDeleteService;
    private final ParagraphHelper paragraphHelper;

    @Override
    @Transactional
    public void processBatch(BatchRequest<ItemPayload> request, UserDetailsImpl userDetails) {
        List<BatchOperation<ItemPayload>> operations = request.batch();
        Set<String> deletedUuids = new HashSet<>();
        boolean firstParagraphUpdated = false;

        Iterator<BatchOperation<ItemPayload>> iterator = operations.iterator();
        while (iterator.hasNext()) {
            BatchOperation<ItemPayload> operation = iterator.next();
            if (operation.method().equals("DELETE") && !operation.body().isEmpty()) {
                Long episodeId = paragraphHelper.extractEpisodeIdFromUrl(operation.url());
                paragraphHelper.validateEpisodeOwnership(episodeId, userDetails);
                paragraphDeleteService.handleDeletedOperation(operation.body(), deletedUuids, episodeId);
                iterator.remove();
            }
        }

        iterator = operations.iterator();
        while (iterator.hasNext()) {
            BatchOperation<ItemPayload> operation = iterator.next();

            Long episodeId = paragraphHelper.extractEpisodeIdFromUrl(operation.url());
            Episode episode = paragraphHelper.validateEpisodeOwnership(episodeId, userDetails);

            switch (operation.method()) {
                case "PUT":
                    if (paragraphPutService.handleModifiedOperation(operation.body(), deletedUuids)) {
                        firstParagraphUpdated = true;
                    }
                    break;
                case "POST":
                    if (paragraphPostService.handleAddedOperation(operation.body(),episode)) {
                        firstParagraphUpdated = true;
                    }
                    break;
                default:
                    throw new ServiceException(ErrorCode.INVALID_BATCH_REQUEST);
            }

            iterator.remove();

            if (firstParagraphUpdated) {
                paragraphHelper.updateEpisodeDescription(episodeId);
            }
        }
    }
}
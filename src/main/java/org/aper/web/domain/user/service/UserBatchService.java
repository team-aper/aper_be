package org.aper.web.domain.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.aper.web.domain.user.service.method.UserBatchPutService;
import org.aper.web.global.batch.dto.BatchRequestDto.BatchOperation;
import org.aper.web.global.batch.dto.BatchRequestDto.BatchRequest;
import org.aper.web.global.batch.service.BatchService;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class UserBatchService<T> implements BatchService<T> {
    private final UserBatchPutService<T> userBatchPutService;

    @PostConstruct
    public void initBatchHandlers() {
        userBatchPutService.initBatchHandlers();
    }

    @Override
    @Transactional
    public void processBatch(BatchRequest<T> request, UserDetailsImpl userDetails) {
        List<BatchOperation<T>> operations = request.batch();
        Iterator<BatchOperation<T>> iterator = operations.iterator();

        while (iterator.hasNext()) {
            BatchOperation<T> operation = iterator.next();
            String url = operation.url();
            List<T> operationDto = operation.body();

            BiConsumer<List<T>, UserDetailsImpl> handler = userBatchPutService.getBatchHandler(url);
            Optional.ofNullable(handler)
                    .orElseThrow(() -> new ServiceException(ErrorCode.INVALID_BATCH_REQUEST))
                    .accept(operationDto, userDetails);
            iterator.remove();
        }
    }
}

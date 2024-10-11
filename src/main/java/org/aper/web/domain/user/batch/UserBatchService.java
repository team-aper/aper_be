package org.aper.web.domain.user.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.aper.web.domain.user.dto.UserRequestDto.*;
import org.aper.web.domain.user.service.UserHistoryService;
import org.aper.web.domain.user.service.UserService;
import org.aper.web.global.batch.dto.BatchRequestDto.BatchOperation;
import org.aper.web.global.batch.dto.BatchRequestDto.BatchRequest;
import org.aper.web.global.batch.service.BatchService;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class UserBatchService<T> implements BatchService<T> {
    private final UserService userService;
    private final UserHistoryService userHistoryService;
    private final ObjectMapper objectMapper;

    private final Map<String, BiConsumer<List<T>, UserDetailsImpl>> batchHandlers = new HashMap<>();

    @PostConstruct
    public void initBatchHandlers() {
        batchHandlers.put("/penname/change", this::handlePenNameChange);
        batchHandlers.put("/image", this::handleImageChange);
        batchHandlers.put("/description/change", this::handleDescriptionChange);
        batchHandlers.put("/contact/email/change", this::handleEmailChange);
        batchHandlers.put("/history", this::handleHistoryChange);
        batchHandlers.put("/class/description/change", this::handleClassDescriptionChange);
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
            BiConsumer<List<T>, UserDetailsImpl> handler = batchHandlers.get(url);
            Optional.ofNullable(handler)
                    .orElseThrow(() -> new ServiceException(ErrorCode.INVALID_BATCH_REQUEST))
                    .accept(operationDto, userDetails);
            iterator.remove();
        }
    }


    private void handlePenNameChange(List<T> operationDto, UserDetailsImpl userDetails) {
        ChangePenNameDto penNameDto = objectMapper.convertValue(operationDto.get(0), ChangePenNameDto.class);
        userService.ChangePenName(userDetails.user(), penNameDto);
    }

    private void handleImageChange(List<T> operationDto, UserDetailsImpl userDetails) {
        MultipartFile imageFile = objectMapper.convertValue(operationDto.get(0), MultipartFile.class);
        userService.changeImage(userDetails.user(), imageFile);
    }

    private void handleDescriptionChange(List<T> operationDto, UserDetailsImpl userDetails) {
        ChangeDescriptionDto descriptionDto = objectMapper.convertValue(operationDto.get(0), ChangeDescriptionDto.class);
        userService.changeDescription(userDetails.user(), descriptionDto);
    }

    private void handleEmailChange(List<T> operationDto, UserDetailsImpl userDetails) {
        ChangeEmailDto changeEmailDto = objectMapper.convertValue(operationDto.get(0), ChangeEmailDto.class);
        userService.changeContactEmail(userDetails.user(), changeEmailDto);
    }

    private void handleHistoryChange(List<T> operationDto, UserDetailsImpl userDetails) {
        List<HistoryRequestDto> historyDtoList = objectMapper.convertValue(operationDto, objectMapper.getTypeFactory().constructCollectionType(List.class, HistoryRequestDto.class));
        userHistoryService.changeHistory(userDetails.user(), historyDtoList);
    }

    private void handleClassDescriptionChange(List<T> operationDto, UserDetailsImpl userDetails) {
        ClassDescriptionRequestDto classDescriptionDto = objectMapper.convertValue(operationDto.get(0), ClassDescriptionRequestDto.class);
        userService.changeClassDescription(userDetails.user(), classDescriptionDto);
    }
}
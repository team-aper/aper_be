package org.aper.web.domain.user.service.method;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.aper.web.domain.image.service.ImageEncoder;
import org.aper.web.domain.image.service.S3ImageService;
import org.aper.web.domain.user.dto.UserRequestDto.*;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.entity.constant.UserBatchTypeEnum;
import org.aper.web.domain.user.service.UserHistoryService;
import org.aper.web.domain.user.service.UserService;
import org.aper.web.global.batch.service.method.BatchPutService;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class UserBatchPutService<T> implements BatchPutService<T> {
    private final UserService userService;
    private final UserHistoryService userHistoryService;
    private final ObjectMapper objectMapper;
    private final S3ImageService s3ImageService;

    private final Map<String, BiConsumer<List<T>, UserDetailsImpl>> batchHandlers = new HashMap<>();

    @PostConstruct
    public void initBatchHandlers() {
        batchHandlers.put(UserBatchTypeEnum.PENNAME.name(), this::handlePenNameChange);
        batchHandlers.put(UserBatchTypeEnum.IMAGE.name(), this::handleImageChange);
        batchHandlers.put(UserBatchTypeEnum.DESCRIPTION.name(), this::handleDescriptionChange);
        batchHandlers.put(UserBatchTypeEnum.CONTACTMAIL.name(), this::handleEmailChange);
        batchHandlers.put(UserBatchTypeEnum.HISTORY.name(), this::handleHistoryChange);
        batchHandlers.put(UserBatchTypeEnum.CLASSDESCRIPTION.name(), this::handleClassDescriptionChange);
    }

    public BiConsumer<List<T>, UserDetailsImpl> getBatchHandler(String url) {
        return batchHandlers.get(url);
    }

    @Override
    public boolean handleModifiedOperation(List<T> itemPayloads, Set<String> deletedUuids) {
        return false;
    }

    private void handlePenNameChange(List<T> operationDto, UserDetailsImpl userDetails) {
        ChangePenNameDto penNameDto = objectMapper.convertValue(operationDto.get(0), ChangePenNameDto.class);
        userService.ChangePenName(userDetails.user(), penNameDto);
    }

    private void handleImageChange(List<T> operationDto, UserDetailsImpl userDetails) {
        ChangeBatchImageDto imageDto = objectMapper.convertValue(operationDto.get(0), ChangeBatchImageDto.class);
        String imageUrl =  imageDto.image();
        if (!s3ImageService.isDefaultImage(imageUrl)) {
            imageUrl = s3ImageService.uploadImageAndGetUrl(imageUrl);
        }
        userService.changeImage(userDetails.user(), imageUrl);
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

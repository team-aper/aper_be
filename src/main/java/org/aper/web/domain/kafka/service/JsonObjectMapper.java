package org.aper.web.domain.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class JsonObjectMapper {
    private final ObjectMapper objectMapper;
    public <T> String writeValueAsString(T data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new ServiceException(ErrorCode.JSON_PROCESSING_ERROR);
        }
    }

    public Map<String, Object> readValue(String message) {
        try {
            return objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.JSON_PROCESSING_ERROR);
        }
    }
}

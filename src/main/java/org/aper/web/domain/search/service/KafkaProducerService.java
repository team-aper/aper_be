package org.aper.web.domain.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.search.entity.dto.SearchDto.KafkaEpisodeDto;
import org.aper.web.domain.search.repository.CustomElasticSearchRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper;
    private final SearchMapper searchMapper;
    private final CustomElasticSearchRepository elasticSearchRepository;

    @Value("${kafka.topic}")
    private String topic;

    public void sendCreate(Episode episode) {
        try {
            KafkaEpisodeDto episodeDto = searchMapper.episodeToKafkaDto(episode, "create");
            String jsonMessage = objectMapper.writeValueAsString(episodeDto);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, jsonMessage);
            producer.send(record);
        } catch (JsonProcessingException e) {
            throw new ServiceException(ErrorCode.JSON_PROCESSING_ERROR);
        }
    }

    public void sendUpdate(Episode episode) {
        try {
            KafkaEpisodeDto episodeDto = searchMapper.episodeToKafkaDto(episode, "update");
            String jsonMessage = objectMapper.writeValueAsString(episodeDto);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, jsonMessage);
            producer.send(record);
        } catch (JsonProcessingException e) {
            throw new ServiceException(ErrorCode.JSON_PROCESSING_ERROR);
        }
    }

    public void sendDelete(Long episodeId) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(Map.of("operation", "delete", "episodeId", episodeId));
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, jsonMessage);
            producer.send(record);
        } catch (JsonProcessingException e) {
            throw new ServiceException(ErrorCode.JSON_PROCESSING_ERROR);
        }
    }

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.consumer-group-id}")
    public void consume(String message) {
        try {
            Map<String, Object> data = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
            String operation = (String) data.get("operation");
            if (operation.equals("delete")) {
                Long episodeId = Long.parseLong(data.get("episodeId").toString());
                elasticSearchRepository.delete(episodeId);
            } else if (operation.equals("update")){
                elasticSearchRepository.update(data);
            }
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.ELASTICSEARCH_CONNECT_FAILED);
        }
    }
}

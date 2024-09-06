package org.aper.web.domain.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.search.entity.dto.SearchDto.KafkaEpisodeDto;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper;
    private final SearchMapper searchMapper;

    @Value("${kafka.topic}")
    private String topic;

    public void sendCreate(Episode episode) {
        try {
            KafkaEpisodeDto episodeDto = searchMapper.episodeToKafkaDto(episode);
            String jsonMessage = objectMapper.writeValueAsString(episodeDto);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, jsonMessage);
            producer.send(record);
        } catch (JsonProcessingException e) {
            throw new ServiceException(ErrorCode.JSON_PROCESSING_ERROR);
        }
    }
}

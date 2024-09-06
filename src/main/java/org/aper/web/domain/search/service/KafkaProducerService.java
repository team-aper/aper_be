package org.aper.web.domain.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.aper.web.global.config.KafkaProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private KafkaProducer<String, String> producer;
    private final KafkaProducerConfig EpisodeProducer;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic}")
    private String topic;

    public void send(KafkaEpisodeDto episodeDto) {
        // DTO 객체를 JSON으로 변환
        String jsonMessage = objectMapper.writeValueAsString(episodeDto);
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, jsonMessage);
        producer.send(record);
    }
}

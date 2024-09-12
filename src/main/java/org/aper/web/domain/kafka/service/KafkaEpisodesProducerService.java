package org.aper.web.domain.kafka.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.aper.web.domain.elasticsearch.repository.EpisodesElasticSearchRepository;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.kafka.entity.dto.KafkaDto.KafkaEpisodeDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaEpisodesProducerService {
    private final KafkaProducer<String, String> producer;
    private final JsonObjectMapper objectMapper;
    private final KafkaMapper kafkaMapper;
    private final EpisodesElasticSearchRepository elasticSearchRepository;

    @Value("${kafka.episodes-topic}")
    private String episodesTopic;

    public void sendCreate(Episode episode) {
        KafkaEpisodeDto episodeDto = kafkaMapper.episodeToKafkaDto(episode, "create");
        String jsonMessage = objectMapper.writeValueAsString(episodeDto);
        ProducerRecord<String, String> record = new ProducerRecord<>(episodesTopic, jsonMessage);
        producer.send(record);
    }

    public void sendUpdate(Episode episode) {
        KafkaEpisodeDto episodeDto = kafkaMapper.episodeToKafkaDto(episode, "update");
        String jsonMessage = objectMapper.writeValueAsString(episodeDto);
        ProducerRecord<String, String> record = new ProducerRecord<>(episodesTopic, jsonMessage);
        producer.send(record);
    }

    public void sendDelete(Long episodeId) {
        String jsonMessage = objectMapper.writeValueAsString(Map.of("operation", "delete", "episodeId", episodeId));
        ProducerRecord<String, String> record = new ProducerRecord<>(episodesTopic, jsonMessage);
        producer.send(record);
    }
}

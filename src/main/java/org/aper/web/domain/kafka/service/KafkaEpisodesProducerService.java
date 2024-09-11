package org.aper.web.domain.kafka.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.aper.web.domain.elasticsearch.repository.EpisodesElasticSearchRepository;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.search.entity.dto.SearchDto.KafkaEpisodeDto;
import org.aper.web.domain.search.service.SearchMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaEpisodesProducerService {
    private final KafkaProducer<String, String> producer;
    private final JsonObjectMapper objectMapper;
    private final SearchMapper searchMapper;
    private final EpisodesElasticSearchRepository elasticSearchRepository;

    @Value("${kafka.episodes-topic}")
    private String episodesTopic;

    public void sendCreate(Episode episode) {
        KafkaEpisodeDto episodeDto = searchMapper.episodeToKafkaDto(episode, "create");
        String jsonMessage = objectMapper.writeValueAsString(episodeDto);
        ProducerRecord<String, String> record = new ProducerRecord<>(episodesTopic, jsonMessage);
        producer.send(record);
    }

    public void sendUpdate(Episode episode) {
        KafkaEpisodeDto episodeDto = searchMapper.episodeToKafkaDto(episode, "update");
        String jsonMessage = objectMapper.writeValueAsString(episodeDto);
        ProducerRecord<String, String> record = new ProducerRecord<>(episodesTopic, jsonMessage);
        producer.send(record);
    }

    public void sendDelete(Long episodeId) {
        String jsonMessage = objectMapper.writeValueAsString(Map.of("operation", "delete", "episodeId", episodeId));
        ProducerRecord<String, String> record = new ProducerRecord<>(episodesTopic, jsonMessage);
        producer.send(record);
    }

    @KafkaListener(topics = "${kafka.episodes-topic}", groupId = "${kafka.consumer-group-id}")
    public void consume(String message) {
        Map<String, Object> data = objectMapper.readValue(message);
        String operation = (String) data.get("operation");
        if (operation.equals("delete")) {
            Long episodeId = Long.parseLong(data.get("episodeId").toString());
            elasticSearchRepository.delete(episodeId);
        }
        if (operation.equals("update")){
            elasticSearchRepository.update(data);
        }
    }
}

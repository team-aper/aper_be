package org.aper.web.domain.kafka.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.aper.web.domain.elasticsearch.repository.UserElasticSearchRepository;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.search.entity.dto.SearchDto.KafkaUserDto;
import org.aper.web.domain.elasticsearch.repository.EpisodesElasticSearchRepository;
import org.aper.web.domain.search.service.SearchMapper;
import org.aper.web.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaUserProducerService {
    private final KafkaProducer<String, String> producer;
    private final SearchMapper searchMapper;
    private final UserElasticSearchRepository elasticSearchRepository;
    private final EpisodeRepository episodeRepository;
    private final KafkaEpisodesProducerService episodesProducerService;
    private final JsonObjectMapper jsonObjectMapper;

    @Value("${kafka.user-topic}")
    private String usersTopic;

    public void sendCreate(User user) {
        KafkaUserDto userDto = searchMapper.userToKafkaDto(user, "create");
        String jsonMessage = jsonObjectMapper.writeValueAsString(userDto);
        ProducerRecord<String, String> record = new ProducerRecord<>(usersTopic, jsonMessage);
        producer.send(record);
    }

    public void sendUpdate(User user) {
        KafkaUserDto userDto = searchMapper.userToKafkaDto(user, "update");
        String jsonMessage = jsonObjectMapper.writeValueAsString(userDto);
        producer.send(new ProducerRecord<>(usersTopic, jsonMessage));
    }

    public void sendDelete(Long userId) {
        List<Episode> episodeList = episodeRepository.findAllByUserId(userId);
        String jsonMessage = jsonObjectMapper.writeValueAsString(Map.of("operation", "delete", "userId", userId));
        producer.send(new ProducerRecord<>(usersTopic, jsonMessage));
        episodeList.forEach(episode -> episodesProducerService.sendDelete(episode.getId()));
    }

    @KafkaListener(topics = "${kafka.user-topic}", groupId = "${kafka.consumer-group-id}")
    public void consume(String message) {
        Map<String, Object> data = jsonObjectMapper.readValue(message);
        String operation = (String) data.get("operation");
        if (operation.equals("delete")) {
            Long episodeId = Long.parseLong(data.get("userId").toString());
            elasticSearchRepository.delete(episodeId);
        }
        if (operation.equals("update")){
            elasticSearchRepository.update(data);
        }
    }
}


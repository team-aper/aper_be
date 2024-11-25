package org.aper.web.domain.kafka.service;

import com.aperlibrary.episode.entity.Episode;
import com.aperlibrary.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.aper.web.domain.elasticsearch.repository.UserElasticSearchRepository;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.kafka.entity.dto.KafkaDto.KafkaUserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaUserProducerService {
    private final KafkaProducer<String, String> producer;
    private final KafkaMapper kafkaMapper;
    private final UserElasticSearchRepository elasticSearchRepository;
    private final EpisodeRepository episodeRepository;
    private final KafkaEpisodesProducerService episodesProducerService;
    private final JsonObjectMapper jsonObjectMapper;

    @Value("${kafka.user-topic}")
    private String usersTopic;

    public void sendCreate(User user) {
        KafkaUserDto userDto = kafkaMapper.userToKafkaDto(user, "create");
        String jsonMessage = jsonObjectMapper.writeValueAsString(userDto);
        ProducerRecord<String, String> record = new ProducerRecord<>(usersTopic, jsonMessage);
        producer.send(record);
    }

    public void sendUpdate(User user) {
        KafkaUserDto userDto = kafkaMapper.userToKafkaDto(user, "update");
        String jsonMessage = jsonObjectMapper.writeValueAsString(userDto);
        producer.send(new ProducerRecord<>(usersTopic, jsonMessage));
    }

    public void sendDelete(Long userId) {
        List<Episode> episodeList = episodeRepository.findAllByUserId(userId);
        String jsonMessage = jsonObjectMapper.writeValueAsString(Map.of("operation", "delete", "userId", userId));
        producer.send(new ProducerRecord<>(usersTopic, jsonMessage));
        episodeList.forEach(episode -> episodesProducerService.sendDelete(episode.getId()));
    }
}


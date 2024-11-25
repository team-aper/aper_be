package org.aper.web.domain.kafka.service;

import com.aperlibrary.episode.entity.Episode;
import com.aperlibrary.story.entity.Story;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
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

    @Value("${kafka.episodes-topic}")
    private String episodesTopic;

    public void sendCreate(Episode episode) {
        KafkaEpisodeDto episodeDto = kafkaMapper.episodeToKafkaDto(episode, episode.getStory(), "create");
        String jsonMessage = objectMapper.writeValueAsString(episodeDto);
        ProducerRecord<String, String> record = new ProducerRecord<>(episodesTopic, jsonMessage);
        producer.send(record);
    }

    public void sendUpdate(Episode episode) {
        KafkaEpisodeDto episodeDto = kafkaMapper.episodeToKafkaDto(episode, episode.getStory(),"update");
        String jsonMessage = objectMapper.writeValueAsString(episodeDto);
        ProducerRecord<String, String> record = new ProducerRecord<>(episodesTopic, jsonMessage);
        producer.send(record);
    }

    public void sendUpdateOnlyStory(Story story) {
        Episode episode = Episode.builder().story(story).build();
        episode.updateOnDisplay();
        KafkaEpisodeDto episodeDto = kafkaMapper.episodeToKafkaDto(episode, episode.getStory(),"onlyStory");
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

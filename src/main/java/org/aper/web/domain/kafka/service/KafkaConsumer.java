package org.aper.web.domain.kafka.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.elasticsearch.repository.EpisodesElasticSearchRepository;
import org.aper.web.domain.elasticsearch.repository.UserElasticSearchRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private final JsonObjectMapper objectMapper;
    private final EpisodesElasticSearchRepository episodeElasticSearchRepository;
    private final UserElasticSearchRepository userElasticSearchRepository;

    @KafkaListener(topics = "${kafka.episodes-topic}", groupId = "${kafka.consumer-group-id}")
    public void episodeConsume(String message) {
        Map<String, Object> data = objectMapper.readValue(message);
        String operation = (String) data.get("operation");
        if (operation.equals("delete")) {
            Long episodeId = Long.parseLong(data.get("episodeId").toString());
            episodeElasticSearchRepository.delete(episodeId);
        }
        if (operation.equals("update")){
            episodeElasticSearchRepository.update(data);
        }
    }

    @KafkaListener(topics = "${kafka.user-topic}", groupId = "${kafka.consumer-group-id}")
    public void userConsume(String message) {
        Map<String, Object> data = objectMapper.readValue(message);
        String operation = (String) data.get("operation");
        if (operation.equals("delete")) {
            Long episodeId = Long.parseLong(data.get("userId").toString());
            userElasticSearchRepository.delete(episodeId);
        }
        if (operation.equals("update")){
            userElasticSearchRepository.update(data);
        }
    }
}

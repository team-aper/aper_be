package org.aper.web.domain.kafka.service;

import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.kafka.entity.dto.KafkaDto.*;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class KafkaMapper {
    public KafkaEpisodeDto episodeToKafkaDto(Episode episode, Story story, String operation) {
        User user = story.getUser();
        return new KafkaEpisodeDto(
                episode.getId(),
                episode.getChapter(),
                episode.getTitle(),
                episode.getDescription(),
                episode.getPublicDate(),
                episode.isOnDisplay(),
                story.getId(),
                story.getTitle(),
                story.getGenre().name(),
                story.isOnDisplay(),
                user.getUserId(),
                user.getPenName(),
                user.getFieldImage(),
                operation
        );
    }

    public KafkaUserDto userToKafkaDto(User user, String operation) {
        return new KafkaUserDto(
                user.getUserId(),
                user.getPenName(),
                operation
        );
    }
}

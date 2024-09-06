package org.aper.web.domain.search.service;

import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.search.entity.dto.SearchDto.*;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.user.entity.User;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SearchMapper {
    public List<AuthorListResponseDto> UserListToAuthorListResponseDto(List<User> userList) {
        return userList.stream()
                .map(user ->
                        new AuthorListResponseDto(
                                user.getPenName(),
                                user.getFieldImage(),
                                user.getDescription(),
                                user.getUserId()))
                .toList();
    }
    public List<StoryListResponseDto> EpisodeListToStoryListResponseDto(List<Object[]> episodeList) {
        return episodeList.stream()
                .map(object -> {
                    Long episodeId = (Long) object[0];
                    String description = (String) object[1];
                    Story story = (Story) object[2];
                    User user = (User) object[3];

                    return new StoryListResponseDto(
                            story.getId(),
                            story.getTitle(),
                            user.getUserId(),
                            story.getGenre().name(),
                            story.getPublicDate(),
                            episodeId,
                            description
                    );
                })
                .toList();
    }
    public KafkaEpisodeDto episodeToKafkaDto(Episode episode) {
        Story story = episode.getStory();
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
                user.getFieldImage()
        );
    }
}

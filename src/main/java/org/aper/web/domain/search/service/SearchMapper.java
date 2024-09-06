package org.aper.web.domain.search.service;

import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.search.entity.document.ElasticSearchEpisodeDocument;
import org.aper.web.domain.search.entity.dto.SearchDto.*;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.user.entity.User;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                            episodeId.toString(),
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
    public List<StoryListResponseDto> documentListToKafkaDto(List<ElasticSearchEpisodeDocument> episodeDocumentList) {
        return episodeDocumentList.stream()
                .map(document -> new StoryListResponseDto(
                            document.getStoryId(),
                            document.getStoryTitle(),
                            document.getUserId(),
                            document.getStoryGenre(),
                            document.getEpisodePublicDate(),
                            document.getEpisodeId(),
                            document.getEpisodeDescription()
                    )
                )
                .toList();
    }

    public ElasticSearchEpisodeDocument mapHitToDocument(SearchHit<ElasticSearchEpisodeDocument> hit) {
        ElasticSearchEpisodeDocument document = hit.getContent();
        Map<String, List<String>> highlightMap = new HashMap<>(hit.getHighlightFields());
        String description = getHighlightDescriptionValue(highlightMap);
        document.setEpisodeDescription(description);
        return document;
    }

    private String getHighlightDescriptionValue(Map<String, List<String>> highlightMap) {
        List<String> highlightValues = highlightMap.get("episodeDescription");
        return (highlightValues != null && !highlightValues.isEmpty()) ? highlightValues.get(0) : null;
    }
}

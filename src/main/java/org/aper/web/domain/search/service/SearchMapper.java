package org.aper.web.domain.search.service;

import org.aper.web.domain.elasticsearch.entity.document.ElasticSearchEpisodeDocument;
import org.aper.web.domain.elasticsearch.entity.document.ElasticSearchUserDocument;
import org.aper.web.domain.search.dto.SearchDto.AuthorListResponseDto;
import org.aper.web.domain.search.dto.SearchDto.AuthorPenNameResponseDto;
import org.aper.web.domain.search.dto.SearchDto.AuthorStoryListResponseDto;
import org.aper.web.domain.search.dto.SearchDto.StoryListResponseDto;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SearchMapper {
    public List<AuthorListResponseDto> UserListToAuthorListResponseDto(List<Object[]> userList) {
        Map<Long, AuthorListResponseDto> uniqueUsers = new LinkedHashMap<>();

        userList.forEach(record -> {
            User user = (User) record[0];
            boolean isSubscribed = (boolean) record[1];
            Long reviewerCount = (Long) record[2];
            Long subscriberCount = (Long) record[3];

            uniqueUsers.computeIfAbsent(user.getUserId(), userId -> new AuthorListResponseDto(
                    user.getPenName(),
                    user.getFieldImage(),
                    user.getDescription(),
                    user.getUserId(),
                    storyListToResponseDto(user.getStoryList()),
                    reviewerCount,
                    subscriberCount,
                    isSubscribed
            ));
        });
        return new ArrayList<>(uniqueUsers.values());
    }

    private List<AuthorStoryListResponseDto> storyListToResponseDto(List<Story> storyList) {
        return storyList.stream()
                .sorted(Comparator.comparing(Story::getPublicDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .limit(3)
                .map(story -> new AuthorStoryListResponseDto(story.getTitle(), story.getId()))
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
                            description,
                            user.getPenName(),
                            user.getFieldImage()
                    );
                })
                .toList();
    }

    public List<StoryListResponseDto> episodesDocumentListToResponseDto(List<ElasticSearchEpisodeDocument> episodeDocumentList) {
        return episodeDocumentList.stream()
                .map(document -> new StoryListResponseDto(
                            document.getStoryId(),
                            document.getStoryTitle(),
                            document.getUserId(),
                            document.getStoryGenre(),
                            document.getEpisodePublicDate(),
                            document.getEpisodeId(),
                            document.getEpisodeDescription(),
                            document.getPenName(),
                            document.getFieldImage()
                    )
                )
                .toList();
    }

    public List<AuthorPenNameResponseDto> userDocumentListToPenNameResponseDto(List<ElasticSearchUserDocument> userList) {
        return userList.stream()
                .map(user ->
                        new AuthorPenNameResponseDto(
                                user.getPenName()))
                .toList();
    }
}

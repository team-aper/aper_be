package org.aper.web.domain.search.service;

import org.aper.web.domain.search.dto.SearchDto.*;
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
                    //String episodeTitle = (String) object[1];
                    String description = (String) object[2];
                    Story story = (Story) object[3];
                    User user = (User) object[4];

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
}

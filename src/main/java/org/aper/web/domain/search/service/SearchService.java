package org.aper.web.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.search.dto.SearchDto.AuthorListResponseDto;
import org.aper.web.domain.search.dto.SearchDto.SearchAuthorResponseDto;
import org.aper.web.domain.search.dto.SearchDto.SearchStoryResponseDto;
import org.aper.web.domain.search.dto.SearchDto.StoryListResponseDto;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final UserRepository userRepository;
    private final EpisodeRepository episodeRepository;

    public SearchStoryResponseDto getSearchStory(
            int page,
            int size,
            StoryGenreEnum genre,
            String filter
    )
    {
        Pageable pageAble = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<Object[]> targetStoriesAndEpisodes = episodeRepository.findAllByTitleAndDescription(pageAble, genre, filter).getContent();


        return new SearchStoryResponseDto(EpisodeListToStoryListResponseDto(targetStoriesAndEpisodes));
    }

    public SearchAuthorResponseDto getSearchAuthor(String penName) {
        List<User> targetAuthors = userRepository.findAllByPenNameContaining(penName);
        return new SearchAuthorResponseDto(UserListToAuthorListResponseDto(targetAuthors));
    }

    private List<AuthorListResponseDto> UserListToAuthorListResponseDto(List<User> userList) {
        return userList.stream()
                .map(user ->
                        new AuthorListResponseDto(
                                user.getPenName(),
                                user.getFieldImage(),
                                user.getDescription(),
                                user.getUserId()))
                .toList();
    }

    private List<StoryListResponseDto> EpisodeListToStoryListResponseDto(List<Object[]> episodeList) {
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
                            story.getGenre(),
                            story.getPublicDate(),
                            episodeId,
                            description
                    );
                })
                .toList();
    }

}
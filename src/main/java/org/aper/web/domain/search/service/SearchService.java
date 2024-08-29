package org.aper.web.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.search.dto.SearchDto.AuthorListResponseDto;
import org.aper.web.domain.search.dto.SearchDto.SearchAuthorResponseDto;
import org.aper.web.domain.search.dto.SearchDto.SearchStoryResponseDto;
import org.aper.web.domain.search.dto.SearchDto.StoryListResponseDto;
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

        List<Episode> targetStoriesAndEpisodes = episodeRepository.findAllByTitleAndDescription(pageAble, genre, filter).getContent();


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

    private List<StoryListResponseDto> EpisodeListToStoryListResponseDto(List<Episode> episodeList) {
        return episodeList.stream()
                .map(episode ->
                        new StoryListResponseDto(
                                episode.getStory().getId(),
                                episode.getStory().getTitle(),
                                episode.getStory().getUser().getUserId(),
                                episode.getStory().getGenre(),
                                episode.getStory().getPublicDate(),
                                episode.getId(),
                                episode.getDescription()
                        ))
                .toList();
    }
}

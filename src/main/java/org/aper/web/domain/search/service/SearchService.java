package org.aper.web.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.search.dto.SearchDto.SearchAuthorResponseDto;
import org.aper.web.domain.search.dto.SearchDto.SearchStoryResponseDto;
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
    private final SearchMapper searchMapper;

    public SearchStoryResponseDto getSearchStory(
            int page,
            int size,
            StoryGenreEnum genre,
            String filter
    )
    {
        Pageable pageAble = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Object[]> targetStoriesAndEpisodes = episodeRepository.findAllByTitleAndDescription(pageAble, genre, filter).getContent();
        return new SearchStoryResponseDto(searchMapper.EpisodeListToStoryListResponseDto(targetStoriesAndEpisodes));
    }

    public SearchAuthorResponseDto getSearchAuthor(int page, int size, String penName) {
        Pageable pageAble = PageRequest.of(page, size);
        List<User> targetAuthors = userRepository.findAllByPenNameContaining(pageAble, penName).getContent();
        return new SearchAuthorResponseDto(searchMapper.UserListToAuthorListResponseDto(targetAuthors));
    }
}
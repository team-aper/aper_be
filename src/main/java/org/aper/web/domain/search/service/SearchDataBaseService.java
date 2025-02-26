package org.aper.web.domain.search.service;

import com.aperlibrary.story.entity.constant.StoryGenreEnum;
import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.search.dto.SearchDto;
import org.aper.web.domain.search.dto.SearchDto.SearchAuthorResponseDto;
import org.aper.web.domain.search.dto.SearchDto.SearchStoryResponseDto;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchDataBaseService implements SearchServiceInterface {
    private final UserRepository userRepository;
    private final EpisodeRepository episodeRepository;
    private final SearchMapper searchMapper;

    @Override
    public SearchStoryResponseDto getSearchStory(
            int page,
            int size,
            StoryGenreEnum genre,
            String filter
    )
    {
        Pageable pageAble = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Object[]> targetStoriesAndEpisodes = episodeRepository.findAllByTitleAndDescription(
                pageAble,
                genre,
                filter)
                .getContent();
        return new SearchStoryResponseDto(searchMapper.EpisodeListToStoryListResponseDto(targetStoriesAndEpisodes));
    }

    @Override
    public SearchDto.SearchPenNameResponseDto getOnlyPenName(int page, int size, String penName) {
        return null;
    }

    @Override
    public SearchAuthorResponseDto getSearchAuthor(int page, int size, String penName, UserDetailsImpl userDetails) {
        return null;
    }
}
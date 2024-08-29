package org.aper.web.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.episode.specification.EpisodeSpecification;
import org.aper.web.domain.search.dto.SearchDto.*;
import org.aper.web.domain.search.specification.StorySpecification;
import org.aper.web.domain.story.constant.StoryGenreEnum;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.domain.story.repository.StoryRepository;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final UserRepository userRepository;
    private final EpisodeRepository episodeRepository;

    public SearchStoryResponseDto getSearchStory(
            int page,   //페이지
            int size,   //데이터 수
            StoryGenreEnum genre,   //스토리 장르
            String filter      //검색 필터
    )
    {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Specification<Episode> spec = EpisodeSpecification.joinWithEpisodeAndApplyFilters(
                genre,
                filter,
                filter,
                filter);

        List<Episode> targetStoriesAndEpisodes = episodeRepository.findAll(spec, pageRequest).getContent();

        List<StoryListResponseDto> responseDtoList = targetStoriesPage.stream()
                .flatMap(story -> story.getEpisodeList().stream()
                        .filter(episode ->
                                (filter == null || episode.getTitle().contains(filter)) &&
                                        (episode.getDescription().contains(filter))
                        )
                        .map(episode -> new StoryListResponseDto(
                                story,
                                episode,
                                episode.getDescription().length() > 80 ? episode.getDescription().substring(0, 80) : episode.getDescription()
                        ))
                )
                .toList();

        return new SearchStoryResponseDto(responseDtoList);
    }

    public SearchAuthorResponseDto getSearchAuthor(String penName) {
        List<User> targetAuthors = userRepository.findAllByPenName(penName);
        return new SearchAuthorResponseDto(UserListToAuthorListResponseDto(targetAuthors));
    }

    private List<AuthorListResponseDto> UserListToAuthorListResponseDto(List<User> userList) {
        return userList.stream()
                .map(AuthorListResponseDto::new)
                .toList();
    }
}

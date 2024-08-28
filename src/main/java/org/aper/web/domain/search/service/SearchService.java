package org.aper.web.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.search.dto.SearchDto.*;
import org.aper.web.domain.search.specification.StorySpecification;
import org.aper.web.domain.story.constant.StoryGenreEnum;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final UserRepository userRepository;

    public SearchStoryResponseDto getSearchStory(
            int page,   //페이지
            int size,   //데이터 수
            StoryGenreEnum genre,   //스토리 장르
            String storyTitle,      //스토리 제목
            String episodeTitle,    //에피소드 제목
            String episodeParagraph)//에피소드 문장 검색
    {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Specification<Story> spec = StorySpecification.joinWithEpisodeAndApplyFilters(
                genre,
                storyTitle,
                episodeTitle,
                episodeParagraph,
                true);

        List<Story> targetStories =

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

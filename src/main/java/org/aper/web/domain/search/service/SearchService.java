package org.aper.web.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.search.dto.SearchDto;
import org.aper.web.domain.search.dto.SearchDto.*;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final UserRepository userRepository;

    public SearchStoryResponseDto getSearchStory(int page, int size, String storyTitle, String episodeTitle, String episodeParagraph) {

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

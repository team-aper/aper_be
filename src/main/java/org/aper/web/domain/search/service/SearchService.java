package org.aper.web.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.search.dto.SearchDto;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final UserRepository userRepository;

    public SearchDto.SearchStoryResponseDto getSearchStory(int page, int size, String storyTitle, String episodeTitle, String episodeParagraph) {

    }

    public SearchDto.SearchAuthorResponseDto getSearchAuthor(String penName) {
        User targetAuthor = userRepository.findBy
    }
}

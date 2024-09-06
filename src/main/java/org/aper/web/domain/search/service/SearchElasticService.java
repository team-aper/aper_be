package org.aper.web.domain.search.service;

import org.aper.web.domain.search.entity.dto.SearchDto;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.springframework.stereotype.Service;

@Service
public class SearchElasticService implements SearchServiceInterface{

    @Override
    public SearchDto.SearchStoryResponseDto getSearchStory(int page, int size, StoryGenreEnum genre, String filter) {
        return null;
    }

    @Override
    public SearchDto.SearchAuthorResponseDto getSearchAuthor(int page, int size, String penName) {
        return null;
    }
}

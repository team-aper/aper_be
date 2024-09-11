package org.aper.web.domain.search.service;

import org.aper.web.domain.search.entity.dto.SearchDto.*;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;

public interface SearchServiceInterface {
    SearchStoryResponseDto getSearchStory(
            int page,
            int size,
            StoryGenreEnum genre,
            String filter
    );
    SearchPenNameResponseDto getOnlyPenName(
            int page,
            int size,
            String penName
    );
    SearchAuthorResponseDto getSearchAuthor(
            int page,
            int size,
            String penName
    );
}

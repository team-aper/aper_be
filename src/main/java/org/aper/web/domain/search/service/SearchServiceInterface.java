package org.aper.web.domain.search.service;

import org.aper.web.domain.search.dto.SearchDto.*;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.global.security.UserDetailsImpl;

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
            String penName,
            UserDetailsImpl userDetails
    );
}

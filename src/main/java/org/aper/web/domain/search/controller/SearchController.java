package org.aper.web.domain.search.controller;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.search.docs.SearchControllerDocs;
import org.aper.web.domain.search.service.SearchService;
import org.aper.web.global.dto.ResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.aper.web.domain.search.dto.SearchDto.*;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController implements SearchControllerDocs {
    private final SearchService searchService;

    @GetMapping("/story")
    public ResponseDto<SearchStoryResponseDto> getSearchStory() {

    }

    @GetMapping("/author")
    public ResponseDto<SearchAuthorResponseDto> getSearchAuthor() {

    }
}

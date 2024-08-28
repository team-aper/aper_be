package org.aper.web.domain.search.controller;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.aper.web.domain.search.docs.SearchControllerDocs;
import org.aper.web.domain.search.service.SearchService;
import org.aper.web.global.dto.ResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.aper.web.domain.search.dto.SearchDto.*;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController implements SearchControllerDocs {
    private final SearchService searchService;

    @GetMapping("/story")
    public ResponseDto<SearchStoryResponseDto> getSearchStory(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String storyTitle,
            @RequestParam(required = false) String episodeTitle,
            @RequestParam(required = false) String episodeParagraph
    ) {
        SearchStoryResponseDto storyResponseDto = searchService.getSearchStory(page, size, storyTitle, episodeTitle, episodeParagraph);
        return ResponseDto.success("이야기 검색 결과", storyResponseDto);
    }

    @GetMapping("/author")
    public ResponseDto<SearchAuthorResponseDto> getSearchAuthor(
            @RequestParam(required = true) String penName
    ) {
        SearchAuthorResponseDto authorResponseDto = searchService.getSearchAuthor(penName);
        return ResponseDto.success("작가 검색 결과", authorResponseDto);
    }
}

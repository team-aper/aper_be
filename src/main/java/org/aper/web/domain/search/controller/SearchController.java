package org.aper.web.domain.search.controller;

import lombok.RequiredArgsConstructor;
import org.aper.web.global.docs.SearchControllerDocs;
import org.aper.web.domain.search.dto.SearchDto.SearchAuthorResponseDto;
import org.aper.web.domain.search.dto.SearchDto.SearchStoryResponseDto;
import org.aper.web.domain.search.service.SearchDataBaseService;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.global.dto.ResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController implements SearchControllerDocs {
    private final SearchDataBaseService searchService;

    @Override
    @GetMapping("/story")
    public ResponseDto<SearchStoryResponseDto> getSearchStory(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) StoryGenreEnum genre,
            @RequestParam String filter
    ) {
        SearchStoryResponseDto storyResponseDto = searchService.getSearchStory(page, size, genre, filter);
        return ResponseDto.success("이야기 검색 결과", storyResponseDto);
    }

    @Override
    @GetMapping("/author")
    public ResponseDto<SearchAuthorResponseDto> getSearchAuthor(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam String penName
    ) {
        SearchAuthorResponseDto authorResponseDto = searchService.getSearchAuthor(page, size, penName);
        return ResponseDto.success("작가 검색 결과", authorResponseDto);
    }
}

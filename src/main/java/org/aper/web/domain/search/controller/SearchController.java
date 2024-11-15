package org.aper.web.domain.search.controller;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.elasticsearch.service.ElasticSyncService;
import org.aper.web.domain.search.dto.SearchDto.*;
import org.aper.web.domain.search.service.SearchElasticService;
import org.aper.web.global.docs.SearchControllerDocs;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController implements SearchControllerDocs {
//    private final SearchDataBaseService searchService;
    private final SearchElasticService searchService;
    private final ElasticSyncService syncService;
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
    @GetMapping("/penname")
    public ResponseDto<SearchPenNameResponseDto> getSearchAuthorOnlyPenName(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam String penName
    ) {
        SearchPenNameResponseDto authorResponseDto = searchService.getOnlyPenName(page, size, penName);
        return ResponseDto.success("필명 검색 결과", authorResponseDto);
    }

    @Override
    @GetMapping("/author")
    public ResponseDto<SearchAuthorResponseDto> getSearchAuthor(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam String penName
    ) {
        SearchAuthorResponseDto authorResponseDto = searchService.getSearchAuthor(page, size, penName, userDetails);
        return ResponseDto.success("작가 검색 결과", authorResponseDto);
    }

    @Override
    @PostMapping("/testsync-episode")
    public ResponseDto<Void> testSyncEpisode() {
        syncService.syncEpisodes();
        return ResponseDto.success("에피소드 엘라스틱 싱크 맞추기 테스트 API 성공");
    }

    @Override
    @PostMapping("/testsync-user")
    public ResponseDto<Void> testSyncUser() {
        syncService.syncUser();
        return ResponseDto.success("유저 엘라스틱 싱크 맞추기 테스트 API 성공");
    }
}

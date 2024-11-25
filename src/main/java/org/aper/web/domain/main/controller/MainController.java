package org.aper.web.domain.main.controller;

import com.aperlibrary.story.entity.constant.StoryGenreEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.main.dto.MainResponseDto.*;
import org.aper.web.domain.main.service.MainService;
import org.aper.web.global.docs.MainControllerDocs;
import org.aper.web.global.dto.ResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
@Slf4j(topic = "메인페이지 컨트롤러")
public class MainController implements MainControllerDocs {
    private final MainService mainService;

    @GetMapping("/curation")
    public ResponseDto<GetCurationsListResponseDto> getCurations(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "3", required = false) int size
    ) {
        GetCurationsListResponseDto responseDto = mainService.getCurations(page, size);
        return ResponseDto.success("Get Curations in main page", responseDto);
    }

    @GetMapping("/user")
    public ResponseDto<GetUsersListResponseDto> getUsers(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "6", required = false) int size
    ) {
        GetUsersListResponseDto responseDto = mainService.getUsers(page, size);
        return ResponseDto.success("Get users in main page", responseDto);
    }

    @GetMapping("/episode")
    public ResponseDto<GetEpisodesListResponseDto> getEpisodes(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "5", required = false) int size,
            @RequestParam(required = false) StoryGenreEnum storyGenre
    ) {
        GetEpisodesListResponseDto responseDto = mainService.getEpisodes(page, size, storyGenre);
        return ResponseDto.success("Get episodes in main page", responseDto);
    }
}

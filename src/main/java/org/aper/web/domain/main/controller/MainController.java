package org.aper.web.domain.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.main.dto.response.GetCurationsResponseDto;
import org.aper.web.domain.main.dto.response.GetEpisodesResponseDto;
import org.aper.web.domain.main.dto.response.GetUsersResponseDto;
import org.aper.web.domain.main.service.MainService;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.global.docs.MainControllerDocs;
import org.aper.web.global.dto.ResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
@Slf4j(topic = "메인페이지 컨트롤러")
public class MainController implements MainControllerDocs {
    private final MainService mainService;

    @GetMapping("/curation")
    public ResponseDto<List<GetCurationsResponseDto>> getCurations(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "3", required = false) int size
    ) {
        final List<GetCurationsResponseDto> responseDtoList = mainService.getCurations(
                page, size
        );
        return ResponseDto.success("Get Curations in main page", responseDtoList);
    }

    @GetMapping("/user")
    public ResponseDto<List<GetUsersResponseDto>> getUsers(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "6", required = false) int size
    ) {
        final List<GetUsersResponseDto> responseDtoList = mainService.getUsers(
                page, size
        );
        return ResponseDto.success("Get users in main page", responseDtoList);
    }

    @GetMapping("/episode")
    public ResponseDto<List<GetEpisodesResponseDto>> getEpisodes(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "5", required = false) int size,
            @RequestParam(required = false) StoryGenreEnum storyGenre
    ) {
        final List<GetEpisodesResponseDto> responseDtoList = mainService.getEpisodes(
                page, size, storyGenre
        );
        return ResponseDto.success("Get episodes in main page", responseDtoList);
    }
}

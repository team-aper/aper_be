package org.aper.web.domain.field.controller;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.field.dto.HomeResponseDto;
import org.aper.web.domain.field.dto.StoriesResponseDto;
import org.aper.web.domain.field.dto.DetailsResponseDto;
import org.aper.web.domain.field.service.FieldService;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/field")
@RequiredArgsConstructor
public class FiledController {
    private final FieldService fieldService;

    @GetMapping("/home/{authorId}")
    public ResponseDto<HomeResponseDto> getFieldHomeData(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long authorId
            ) {
        HomeResponseDto fieldHomeData = fieldService.getFieldHomeData(userDetails, authorId);
        return ResponseDto.success("작가 필드 홈 데이터", fieldHomeData);
    }

    @GetMapping("/stories/{authorId}")
    public ResponseDto<StoriesResponseDto> getStoriesData(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long authorId
            ) {
        StoriesResponseDto fieldStoriesData = fieldService.getStoriesData(userDetails, authorId);
        return ResponseDto.success("작가 필드 이야기 별 목록 데이터", fieldStoriesData);
    }

    @GetMapping("/details/{authorId}")
    public ResponseDto<DetailsResponseDto> getDetailsData(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long authorId
    ) {
        DetailsResponseDto fieldDetailsData = fieldService.getDetailsData(userDetails, authorId);
        return ResponseDto.success("작가 필드 작가 정보 데이터", fieldDetailsData);
    }
}

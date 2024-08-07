package org.aper.web.domain.field.controller;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.field.dto.HomeResponseDto;
import org.aper.web.domain.field.dto.StoriesDetailsResponseDto;
import org.aper.web.domain.field.dto.DetailsResponseDto;
import org.aper.web.domain.field.dto.StoriesResponseDto;
import org.aper.web.domain.field.service.FieldService;
import org.aper.web.domain.user.service.UserService;
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
public class FieldController {
    private final FieldService fieldService;

    @GetMapping("/home/{authorId}") //작가가 작성한 에피소드를 최신순으로 보내줌
    public ResponseDto<HomeResponseDto> getFieldHomeData(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long authorId
            ) {
        HomeResponseDto fieldHomeData = fieldService.getFieldHomeData(userDetails, authorId);
        return ResponseDto.success("작가 필드 홈 데이터", fieldHomeData);
    }

    @GetMapping("/stories/{authorId}")  //작가가 생성한 이야기를 최신순으로 보내줌
    public ResponseDto<StoriesResponseDto> getStoriesData(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long authorId
            ) {
        StoriesResponseDto fieldStoriesData = fieldService.getStoriesData(userDetails, authorId);
        return ResponseDto.success("작가 필드 이야기 별 목록 데이터", fieldStoriesData);
    }

    @GetMapping("/details/{authorId}")  //작가의 필명, 이메일
    public ResponseDto<DetailsResponseDto> getDetailsData(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long authorId
    ) {
        DetailsResponseDto fieldDetailsData = fieldService.getDetailsData(userDetails, authorId);
        return ResponseDto.success("작가 필드 작가 정보 데이터", fieldDetailsData);
    }
}

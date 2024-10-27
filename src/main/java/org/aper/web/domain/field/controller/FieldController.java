package org.aper.web.domain.field.controller;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.field.dto.FieldResponseDto.DetailsResponseDto;
import org.aper.web.domain.field.dto.FieldResponseDto.FieldHeaderResponseDto;
import org.aper.web.domain.field.dto.FieldResponseDto.HomeResponseDto;
import org.aper.web.domain.field.dto.FieldResponseDto.StoriesResponseDto;
import org.aper.web.domain.field.service.FieldService;
import org.aper.web.domain.user.dto.UserResponseDto.*;
import org.aper.web.domain.user.service.UserHistoryService;
import org.aper.web.global.docs.FieldControllerDocs;
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
public class FieldController implements FieldControllerDocs {
    private final FieldService fieldService;
    private final UserHistoryService userHistoryService;

    @Override
    @GetMapping("/header/{authorId}")
    public ResponseDto<FieldHeaderResponseDto> getAuthorInfo(@PathVariable Long authorId) {
        FieldHeaderResponseDto authorInfo = fieldService.getAuthorInfo(authorId);
        return ResponseDto.success("작가 필드 헤더 데이터", authorInfo);
    }

    @Override
    @GetMapping("/home/{authorId}") //작가가 작성한 에피소드를 최신순으로 보내줌
    public ResponseDto<HomeResponseDto> getFieldHomeData(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long authorId) {
        HomeResponseDto fieldHomeData = fieldService.getFieldHomeData(userDetails, authorId);
        return ResponseDto.success("작가 필드 홈 데이터", fieldHomeData);
    }

    @Override
    @GetMapping("/stories/{authorId}")  //작가가 생성한 이야기를 최신순으로 보내줌
    public ResponseDto<StoriesResponseDto> getStoriesData(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long authorId) {
        StoriesResponseDto fieldStoriesData = fieldService.getStoriesData(userDetails, authorId);
        return ResponseDto.success("작가 필드 이야기 별 목록 데이터", fieldStoriesData);
    }

    @Override
    @GetMapping("/details/{authorId}")  //작가의 필명, 이메일
    public ResponseDto<DetailsResponseDto> getDetailsData(
            @PathVariable Long authorId) {
        DetailsResponseDto fieldDetailsData = fieldService.getDetailsData(authorId);
        return ResponseDto.success("작가 필드 작가 정보 데이터", fieldDetailsData);
    }

    @Override
    @GetMapping("/history/{authorId}")
    public ResponseDto<HistoryResponseDto> getHistoriesData(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long authorId) {
        HistoryResponseDto historyList = fieldService.getHistory(authorId);
        return ResponseDto.success("작가 이력 정보", historyList);
    }

    @Override
    @GetMapping("/class/description/{authorId}")
    public ResponseDto<ClassDescriptionResponseDto> getClassDescription(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long authorId) {
        ClassDescriptionResponseDto classDescription = fieldService.getClassDescription(authorId);
        return ResponseDto.success("1:1 수업 소개 정보", classDescription);
    }


}

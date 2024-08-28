package org.aper.web.domain.story.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.dto.EpisodeResponseDto;
import org.aper.web.domain.story.dto.StoryRequestDto.CoverChangeDto;
import org.aper.web.domain.story.dto.StoryRequestDto.StoryCreateDto;
import org.aper.web.domain.story.dto.StoryResponseDto.CreatedStoryDto;
import org.aper.web.domain.story.dto.StoryResponseDto.GetStoryDto;
import org.aper.web.domain.story.service.StoryService;
import org.aper.web.global.docs.StoryControllerDocs;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/story")
@RequiredArgsConstructor
public class StoryController implements StoryControllerDocs {
    private final StoryService storyService;

    @Override
    @PostMapping
    public ResponseDto<CreatedStoryDto> createStory(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody StoryCreateDto storyCreateDto){
        CreatedStoryDto createdStoryData = storyService.createStory(userDetails, storyCreateDto);
        return ResponseDto.success(userDetails.user().getPenName() + "님의 스토리가 생성되었습니다.", createdStoryData);
    }

    @Override
    @GetMapping("/{storyId}")
    public ResponseDto<GetStoryDto> getStory(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long storyId) {
        GetStoryDto storyData = storyService.getStory(userDetails, storyId);
        return ResponseDto.success("스토리 " + storyId + " 데이터", storyData);
    }

    @Override
    @PutMapping("/{storyId}/cover")
    public ResponseDto<Void> changeCover(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long storyId,
            @RequestBody CoverChangeDto coverChangeDto) {
        storyService.changeCover(userDetails, storyId, coverChangeDto);
        return ResponseDto.success("스토리 커버 변경에 성공했습니다.");
    }

    @Override
    @PutMapping("/{storyId}/publish")
    public ResponseDto<Void> changeStoryPublicStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long storyId
    ) {
        storyService.changePublicStatus(storyId, userDetails);
        return ResponseDto.success("스토리의 공개 상태 변경에 성공했습니다.");
    }

    @Override
    @DeleteMapping("{storyId}")
    public ResponseDto<Void> deleteStory(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long storyId) {
        storyService.deleteStory(userDetails, storyId);
        return ResponseDto.success("스토리가 삭제되었습니다.");
    }

    @PostMapping("/{storyId}/episode/{chapter}")
    public ResponseDto<EpisodeResponseDto.CreatedEpisodeDto> createEpisode(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long storyId,
            @PathVariable Long chapter
    ){
        EpisodeResponseDto.CreatedEpisodeDto createdEpisodeData = storyService.createEpisode(userDetails, storyId, chapter);
        return ResponseDto.success("에피소드를 생성하였습니다.", createdEpisodeData);
    }
}

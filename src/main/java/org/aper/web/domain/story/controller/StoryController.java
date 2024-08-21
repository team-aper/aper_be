package org.aper.web.domain.story.controller;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.story.docs.StoryControllerDocs;
import org.aper.web.domain.story.dto.StoryRequestDto;
import org.aper.web.domain.story.service.StoryService;
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
    public ResponseDto<Void> createStory(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody StoryRequestDto.StoryCreateDto storyCreateDto){
        storyService.createStory(userDetails, storyCreateDto);
        return ResponseDto.success(userDetails.user().getPenName() + "님의 스토리가 생성되었습니다.");
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
}

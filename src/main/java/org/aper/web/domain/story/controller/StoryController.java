package org.aper.web.domain.story.controller;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.story.service.StoryService;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/story")
@RequiredArgsConstructor
public class StoryController {
    private final StoryService storyService;
    @PutMapping("/{storyId}/publish")
    public ResponseDto<Void> changeStoryPublicStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long episodeId
    ) {
        storyService.changePublicStatus(episodeId, userDetails);
        return ResponseDto.success("스토리의 공개 상태 변경에 성공했습니다.");
    }
}

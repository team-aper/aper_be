package org.aper.web.domain.story.docs;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.aper.web.domain.story.dto.StoryRequestDto.*;
import org.aper.web.domain.story.dto.StoryResponseDto.*;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "스토리 관련 API")
public interface StoryControllerDocs {

    @PostMapping
    ResponseDto<Void> createStory(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody StoryCreateDto storyCreateDto);

    @GetMapping("/{storyId}")
    ResponseDto<GetStoryDto> getStory(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long storyId);

    @PutMapping("/{storyId}/cover")
    ResponseDto<Void> changeCover(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long storyId, @RequestBody CoverChangeDto coverChangeDto);

    @PutMapping("/{storyId}/publish")
    ResponseDto<Void> changeStoryPublicStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long storyId);

    @DeleteMapping("/{storyId}")
    ResponseDto<Void> deleteStory(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long storyId);

}


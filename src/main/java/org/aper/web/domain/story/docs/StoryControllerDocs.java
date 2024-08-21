package org.aper.web.domain.story.docs;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.aper.web.domain.story.dto.StoryRequestDto.*;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "스토리 관련 API")
public interface StoryControllerDocs {


    @PostMapping
    ResponseDto<Void> createStory(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody StoryCreateDto storyCreateDto);

    @PutMapping("/{storyId}/publish")
    ResponseDto<Void> changeStoryPublicStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long storyId);
}

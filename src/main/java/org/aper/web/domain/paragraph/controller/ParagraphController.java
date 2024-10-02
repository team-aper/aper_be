package org.aper.web.domain.paragraph.controller;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.paragraph.dto.ParagraphRequestDto;
import org.aper.web.domain.paragraph.service.ParagraphService;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/episode/{episodeId}/paragraph")
@RequiredArgsConstructor
public class ParagraphController {

    private final ParagraphService paragraphService;

    @PostMapping("/batch")
    public ResponseDto<Void> processBatch(
            @PathVariable Long episodeId,
            @RequestBody ParagraphRequestDto.BatchPayload payload,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        paragraphService.processBatch(episodeId, payload, userDetails);
        return ResponseDto.success("자동저장 되었습니다.");
    }
}

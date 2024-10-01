package org.aper.web.domain.paragraph.controller;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.paragraph.dto.ParagraphRequestDto;
import org.aper.web.domain.paragraph.service.ParagraphService;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/episodes/{episodeId}/paragraphs")
@RequiredArgsConstructor
public class ParagraphController {

    private final ParagraphService paragraphService;

    @PostMapping("/batch")
    public void processBatch(
            @PathVariable Long episodeId,
            @RequestBody ParagraphRequestDto.BatchPayload payload,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        paragraphService.processBatch(episodeId, payload.modified(), payload.added(), payload.deleted(), userDetails);
    }
}

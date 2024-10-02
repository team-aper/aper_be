package org.aper.web.domain.paragraph.controller;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.paragraph.dto.ParagraphRequestDto.BatchRequest;
import org.aper.web.domain.paragraph.service.ParagraphService;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class ParagraphController {

    private final ParagraphService paragraphService;

    @PostMapping
    public ResponseDto<Void> processBatch(
            @RequestBody BatchRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        paragraphService.processBatch(request, userDetails);
        return ResponseDto.success("자동저장 되었습니다.");
    }

}

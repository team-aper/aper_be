package org.aper.web.domain.episode.controller;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.service.EpisodeService;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/episode")
@RequiredArgsConstructor
public class EpisodeController {
    private final EpisodeService episodeService;
    @PutMapping("/{episodeId}/publish")
    public ResponseDto<Void> changePublicStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long episodeId
            ) {
        episodeService.changePublicStatus(episodeId, userDetails);
        return ResponseDto.success("에피소드의 공개 상태 변경에 성공했습니다.");
    }
}

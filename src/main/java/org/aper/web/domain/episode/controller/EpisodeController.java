package org.aper.web.domain.episode.controller;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.dto.EpisodeRequestDto.TextChangeDto;
import org.aper.web.domain.episode.dto.EpisodeRequestDto.TitleChangeDto;
import org.aper.web.domain.episode.service.EpisodeService;
import org.aper.web.global.docs.EpisodeControllerDocs;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/episode")
@RequiredArgsConstructor
public class EpisodeController implements EpisodeControllerDocs {
    private final EpisodeService episodeService;

    @PutMapping("/{episodeId}/title")
    public ResponseDto<Void> changeTitle(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long episodeId,
            @RequestBody TitleChangeDto titleChangeDto){
        episodeService.changeTitle(userDetails, episodeId, titleChangeDto);
        return ResponseDto.success("제목 변경에 성공하였습니다.");
    };

    @PutMapping("/{episodeId}/text")
    public ResponseDto<Void> changeText(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long episodeId,
            @RequestBody TextChangeDto textChangeDto){
        episodeService.changeText(userDetails, episodeId, textChangeDto);
        return ResponseDto.success("텍스트 저장에 성공하였습니다.");
    }

    @PutMapping("/{episodeId}/publish")
    public ResponseDto<Void> changeEpisodePublicStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long episodeId) {
        episodeService.changePublicStatus(episodeId, userDetails);
        return ResponseDto.success("에피소드의 공개 상태 변경에 성공했습니다.");
    }

}

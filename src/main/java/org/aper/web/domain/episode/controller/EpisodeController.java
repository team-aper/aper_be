package org.aper.web.domain.episode.controller;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.dto.EpisodeRequestDto.DeleteEpisodeDto;
import org.aper.web.domain.episode.dto.EpisodeRequestDto.TitleChangeDto;
import org.aper.web.domain.episode.dto.EpisodeResponseDto.EpisodeHeaderDto;
import org.aper.web.domain.episode.dto.EpisodeResponseDto.EpisodeTextDto;
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

    @Override
    @GetMapping("/{episodeId}/header")
    public ResponseDto<EpisodeHeaderDto> getEpisodeHeader(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long episodeId){
        EpisodeHeaderDto episodeData = episodeService.getEpisodeHeader(userDetails, episodeId);
        return ResponseDto.success("episode " + episodeId + " header data", episodeData);
    }

    @Override
    @GetMapping("/{episodeId}/text")
    public ResponseDto<EpisodeTextDto> getEpisodeText(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long episodeId){
        EpisodeTextDto episodeData = episodeService.getEpisodeText(userDetails, episodeId);
        return ResponseDto.success("episode " + episodeId + " text data", episodeData);
    }

    @Override
    @PutMapping("/{episodeId}/title")
    public ResponseDto<Void> changeTitle(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long episodeId,
            @RequestBody TitleChangeDto titleChangeDto){
        episodeService.changeTitle(userDetails, episodeId, titleChangeDto);
        return ResponseDto.success("제목 변경에 성공하였습니다.");
    }
    @Override
    @DeleteMapping("/{episodeId}")
    public ResponseDto<Void> deleteEpisode(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long episodeId,
            @RequestBody DeleteEpisodeDto deleteEpisodeDto){
        episodeService.deleteEpisode(userDetails, episodeId, deleteEpisodeDto);
        return ResponseDto.success("에피소드를 삭제하였습니다.");
    }

    @Override
    @PutMapping("/{episodeId}/publish")
    public ResponseDto<Void> changeEpisodePublicStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long episodeId) {
        episodeService.changePublicStatus(episodeId, userDetails);
        return ResponseDto.success("에피소드의 공개 상태 변경에 성공했습니다.");
    }
}

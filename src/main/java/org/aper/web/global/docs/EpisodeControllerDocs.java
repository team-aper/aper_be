package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aper.web.domain.episode.dto.EpisodeRequestDto.DeleteEpisodeDto;
import org.aper.web.domain.episode.dto.EpisodeRequestDto.TitleChangeDto;
import org.aper.web.domain.episode.dto.EpisodeResponseDto.EpisodeHeaderDto;
import org.aper.web.domain.episode.dto.EpisodeResponseDto.EpisodeTextDto;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Episode", description = "에피소드 관련 API")
public interface EpisodeControllerDocs {

    @Operation(summary = "에피소드 헤더 정보 가져오기", description = "특정 에피소드의 헤더 정보를 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 에피소드 헤더 정보를 가져왔습니다.",
                    content = @Content(schema = @Schema(implementation = EpisodeHeaderDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 에피소드 또는 스토리 (ErrorCode 목록: \n" +
                    "E001 - 존재하지 않는 에피소드입니다,\n" +
                    "S001 - 존재하지 않는 이야기입니다)",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "공개되지 않은 에피소드 또는 스토리의 작성자가 아님 (ErrorCode 목록: \n" +
                    "E003 - 공개되지 않은 에피소드입니다,\n" +
                    "S002 - 해당 이야기의 작성자가 아닙니다)",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/{episodeId}/header")
    ResponseDto<EpisodeHeaderDto> getEpisodeHeader(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long episodeId);

    @Operation(summary = "에피소드 텍스트 정보 가져오기", description = "특정 에피소드의 텍스트 정보를 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 에피소드 텍스트 정보를 가져왔습니다.",
                    content = @Content(schema = @Schema(implementation = EpisodeTextDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 에피소드 또는 스토리 (ErrorCode 목록: \n" +
                    "E001 - 존재하지 않는 에피소드입니다,\n" +
                    "S001 - 존재하지 않는 이야기입니다)",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "공개되지 않은 에피소드 또는 스토리의 작성자가 아님 (ErrorCode 목록: \n" +
                    "E003 - 공개되지 않은 에피소드입니다,\n" +
                    "S002 - 해당 이야기의 작성자가 아닙니다)",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/{episodeId}/text")
    ResponseDto<EpisodeTextDto> getEpisodeText(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long episodeId);

    @Operation(summary = "에피소드 제목 변경", description = "특정 에피소드의 제목을 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "제목 변경에 성공하였습니다.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 에피소드 또는 스토리 (ErrorCode 목록: \n" +
                    "E001 - 존재하지 않는 에피소드입니다,\n" +
                    "S001 - 존재하지 않는 이야기입니다)",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "에피소드 또는 스토리의 작성자가 아님 (ErrorCode 목록: \n" +
                    "E002 - 해당 에피소드의 작성자가 아닙니다,\n" +
                    "S002 - 해당 이야기의 작성자가 아닙니다)",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PutMapping("/{episodeId}/title")
    ResponseDto<Void> changeTitle(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long episodeId,
            @RequestBody TitleChangeDto titleChangeDto);

    @Operation(summary = "에피소드 삭제", description = "특정 에피소드를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "에피소드를 삭제하였습니다.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 에피소드 또는 스토리 (ErrorCode 목록: \n" +
                    "E001 - 존재하지 않는 에피소드입니다,\n" +
                    "S001 - 존재하지 않는 이야기입니다)",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "에피소드 또는 스토리의 작성자가 아님 (ErrorCode 목록: \n" +
                    "E002 - 해당 에피소드의 작성자가 아닙니다,\n" +
                    "S002 - 해당 이야기의 작성자가 아닙니다)",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @DeleteMapping("/{episodeId}")
    ResponseDto<Void> deleteEpisode(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long episodeId,
            @RequestBody DeleteEpisodeDto deleteEpisodeDto);

    @Operation(summary = "에피소드 공개 상태 변경", description = "특정 에피소드의 공개 상태를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "에피소드의 공개 상태 변경에 성공했습니다.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 에피소드 또는 스토리 (ErrorCode 목록: \n" +
                    "E001 - 존재하지 않는 에피소드입니다,\n" +
                    "S001 - 존재하지 않는 이야기입니다)",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "에피소드 또는 스토리의 작성자가 아님 (ErrorCode 목록: \n" +
                    "E002 - 해당 에피소드의 작성자가 아닙니다,\n" +
                    "S002 - 해당 이야기의 작성자가 아닙니다)",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PutMapping("/{episodeId}/publish")
    ResponseDto<Void> changeEpisodePublicStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long episodeId);
}

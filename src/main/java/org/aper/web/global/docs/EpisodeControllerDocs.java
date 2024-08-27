package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "에피소드 관련 API", description = "에피소드 공개 상태 관리 API 목록")
public interface EpisodeControllerDocs {

    @Operation(summary = "에피소드 공개 상태 변경", description = "특정 에피소드의 공개 상태를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "에피소드 공개 상태 변경 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "만료된 accessToken", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "에피소드를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    ResponseDto<Void> changeEpisodePublicStatus(
            @Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "공개 상태를 변경할 에피소드의 ID", required = true) @PathVariable Long episodeId
    );
}

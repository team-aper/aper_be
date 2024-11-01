package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aper.web.global.dto.ErrorResponseDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "Notification", description = "알림 구독 관련 API 목록")
public interface NotificationControllerDocs {

    @Operation(summary = "SSE 구독", description = "지정된 사용자 ID에 대해 알림을 구독합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "구독 성공", content = @Content(schema = @Schema(implementation = SseEmitter.class))),
        @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    SseEmitter subscribe(
        @Parameter(description = "사용자 ID", required = true) @PathVariable Long userId
    );
}

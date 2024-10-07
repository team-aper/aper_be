package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aper.web.domain.paragraph.dto.ParagraphRequestDto.BatchRequest;
import org.aper.web.global.dto.ErrorResponseDto;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Paragraph Batch", description = "문단 자동 저장을 위한 배치 처리 API")
public interface ParagraphControllerDocs {

    @Operation(summary = "문단 'POST', 'PUT', 'DELETE' 요청에 대한 batch 처리",
            description = "문단 자동 저장 기능을 위한 배치 요청을 처리하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 배치 요청을 처리했습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "C002 - 유효성 검사 실패,\n" +
                    "B001 - Invalid batch request,\n" +
                    "B002 - Invalid URL format,\n" +
                    "P002 - 이미 존재하는 UUID 입니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 문단 또는 에피소드 (ErrorCode 목록: \n" +
                    "P001 - 존재하지 않는 문단입니다,\n" +
                    "E001 - 존재하지 않는 에피소드입니다,\n" +
                    "S001 - 존재하지 않는 이야기입니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "에피소드 또는 스토리의 작성자가 아님 (ErrorCode 목록: \n" +
                    "E002 - 해당 에피소드의 작성자가 아닙니다,\n" +
                    "S002 - 해당 이야기의 작성자가 아닙니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<Void> processBatch(
            @RequestBody BatchRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails);

}

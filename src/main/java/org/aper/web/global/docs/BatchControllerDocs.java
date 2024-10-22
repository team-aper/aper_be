package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aper.web.domain.paragraph.dto.ParagraphRequestDto.ItemPayload;
import org.aper.web.global.batch.dto.BatchRequestDto.BatchRequest;
import org.aper.web.global.dto.ErrorResponseDto;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Batch", description = "배치 처리 API")
public interface BatchControllerDocs {

    @Operation(summary = "문단 'POST', 'PUT', 'DELETE' 요청에 대한 batch 처리",
            description = "배치 요청을 처리하는 API입니다.")
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
    ResponseDto<Void> processParagraphBatch(
            @RequestBody BatchRequest<ItemPayload> request,
            @AuthenticationPrincipal UserDetailsImpl userDetails);

    @Operation(
            summary = "유저 정보 'PUT' 요청에 대한 배치 처리",
            description = "유저 정보와 관련된 여러 'PUT' 요청을 배치로 처리하는 API입니다. 여러 수정 작업을 한 번에 처리할 수 있습니다.<br>" +
            "이미지의 경우 base64로 인코딩 후 헤더 정보까지 같이 보내면 됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 배치 요청을 처리했습니다."),
            @ApiResponse(responseCode = "400", description = """
            잘못된 요청입니다. ErrorCode 목록:
            - C002: 유효성 검사 실패 (Invalid input value),
            - B001: 잘못된 배치 요청 (Invalid batch request),
            - B002: 잘못된 URL 형식 (Invalid URL format),
            - UH002: 해당 이력을 작성 할 수 있는 권한이 없습니다,
            - UH003: "존재하지 않는 종류의 이력 입니다,
            - UH004: "존재하지 않는 종료 형태입니다,
            - UH005: "존재하지 않는 시작 형태입니다.""",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = """
            리소스를 찾을 수 없습니다. ErrorCode 목록:
            - U002: 등록되지 않은 회원 (User not found),
            - UH001: 존재하지 않는 작가 이력 (History not found).""",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = """
            접근 권한이 없습니다. ErrorCode 목록:
            - A004: 유효하지 않은 엑세스 토큰 (Invalid access token),
            - A009: 사용자의 권한을 찾을 수 없음 (Auth not found).""",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = """
            서버 내부 오류입니다. ErrorCode 목록:
            - C001: 내부 서버 오류 (Internal server error),
            - AWS001: S3 업로드 에러 (S3 upload error occurred),
            - ES001: JSON 변환 에러 (JSON processing error).""",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    <T> ResponseDto<Void> processUserInfoBatch(
            @RequestBody BatchRequest<T> request,
            @AuthenticationPrincipal UserDetailsImpl userDetails);
}

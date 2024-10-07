package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aper.web.domain.field.dto.FieldResponseDto.*;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Field", description = "작가의 필드에서 필요한 API")
public interface FieldControllerDocs {

    @Operation(summary = "필드 헤더 작가 정보 get API", description = "필드 페이지 상단에 필명, 작가 소개, 필드 이미지를 보여줌")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "필드 헤더 작가 정보 조회 성공", content = @Content(schema = @Schema(implementation = FieldHeaderResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음 (ErrorCode: U002 - 등록되지 않은 회원입니다)", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    ResponseDto<FieldHeaderResponseDto> getAuthorInfo(@PathVariable Long authorId);

    @Operation(summary = "필드 홈 get API", description = "토큰 필수 x, 본인의 필드일 경우 모든 에피소드를 보여줌")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "필드 홈 데이터 조회 성공", content = @Content(schema = @Schema(implementation = HomeResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음 (ErrorCode: U002 - 등록되지 않은 회원입니다)", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    ResponseDto<HomeResponseDto> getFieldHomeData(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long authorId);

    @Operation(summary = "이야기 별 목록 get API", description = "토큰 필수 x, 본인의 필드일 경우 모든 스토리를 보여줌")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이야기 목록 조회 성공", content = @Content(schema = @Schema(implementation = StoriesResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음 (ErrorCode: U002 - 등록되지 않은 회원입니다)", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    ResponseDto<StoriesResponseDto> getStoriesData(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long authorId);

    @Operation(summary = "작가 정보 get API", description = "토큰 필수 x")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "작가 정보 조회 성공", content = @Content(schema = @Schema(implementation = DetailsResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음 (ErrorCode: U002 - 등록되지 않은 회원입니다)", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    ResponseDto<DetailsResponseDto> getDetailsData(@PathVariable Long authorId);
}

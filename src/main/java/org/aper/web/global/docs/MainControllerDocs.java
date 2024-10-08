package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aper.web.domain.main.dto.MainResponseDto.GetCurationsListResponseDto;
import org.aper.web.domain.main.dto.MainResponseDto.GetEpisodesListResponseDto;
import org.aper.web.domain.main.dto.MainResponseDto.GetUsersListResponseDto;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.global.dto.ErrorResponseDto;
import org.aper.web.global.dto.ResponseDto;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Main", description = "메인 페이지 API")
public interface MainControllerDocs {

    @Operation(summary = "추천하는 이야기 API", description = "메인 페이지에 표시되는 이야기 큐레이션 목록을 가져옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추천하는 이야기 목록 가져오기 성공", content = @Content(schema = @Schema(implementation = GetCurationsListResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode: C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음 (ErrorCode: A009 - 사용자의 권한을 찾을 수 없습니다)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<GetCurationsListResponseDto> getCurations(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "3", required = false) int size
    );

    @Operation(summary = "지금 주목받는 작가의 필드 API", description = "메인 페이지에 표시되는 작가 목록을 가져옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "작가 목록 가져오기 성공", content = @Content(schema = @Schema(implementation = GetUsersListResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode: C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음 (ErrorCode: A009 - 사용자의 권한을 찾을 수 없습니다)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<GetUsersListResponseDto> getUsers(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "6", required = false) int size
    );

    @Operation(summary = "새로운 이야기 API", description = "메인 페이지에 표시되는 에피소드 목록을 가져옵니다. 장르를 선택할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "에피소드 목록 가져오기 성공", content = @Content(schema = @Schema(implementation = GetEpisodesListResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode: C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음 (ErrorCode: A009 - 사용자의 권한을 찾을 수 없습니다)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<GetEpisodesListResponseDto> getEpisodes(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "5", required = false) int size,
            @RequestParam(required = false) StoryGenreEnum storyGenre
    );
}

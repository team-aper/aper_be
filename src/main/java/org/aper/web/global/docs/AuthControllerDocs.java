package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.aper.web.domain.user.dto.UserRequestDto.LoginRequestDto;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.jwt.dto.UserInfo;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.UnsupportedEncodingException;

@Tag(name = "Auth", description = "사용자 인증 및 토큰 관리 API 목록")
public interface AuthControllerDocs {

    @Operation(summary = "사용자 로그인", description = "사용자의 로그인 요청을 처리하고 액세스 및 리프레시 토큰을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = UserInfo.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 로그인 요청", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "만료된 엑세스 토큰", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    ResponseDto<UserInfo> login(
            @Parameter(description = "로그인 요청 데이터", required = true) @RequestBody @Valid LoginRequestDto loginRequestDto,
            @Parameter(hidden = true) HttpServletResponse response
    ) throws UnsupportedEncodingException;

    @Operation(summary = "토큰 재발급", description = "사용자의 리프레시 토큰을 통해 새로운 액세스 및 리프레시 토큰을 재발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "토큰 재발급 실패", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    ResponseDto<Void> reissue(
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(hidden = true) HttpServletResponse response
    ) throws UnsupportedEncodingException;
}

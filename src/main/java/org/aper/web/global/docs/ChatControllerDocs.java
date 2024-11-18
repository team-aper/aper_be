package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aper.web.domain.chat.dto.ChatParticipatingResponseDto;
import org.aper.web.global.dto.ErrorResponseDto;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "chat-controller", description = "튜터와 채팅방 생성 및 관리 API")
public interface ChatControllerDocs {

//    @Operation(summary = "튜터와 채팅방 만들기", description = "이미 생성된 채팅방인지 확인하고, 생성되어 있지 않다면 채팅방을 생성합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "채팅방 생성 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode: C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
//            @ApiResponse(responseCode = "401", description = "인증 실패 (ErrorCode: A001 - 인증에 실패하였습니다)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
//            @ApiResponse(responseCode = "403", description = "접근 권한 없음 (ErrorCode: A009 - 사용자의 권한을 찾을 수 없습니다)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
//            @ApiResponse(responseCode = "404", description = "튜터를 찾을 수 없음 (ErrorCode: CH001 - 존재하지 않는 튜터입니다)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
//            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
//    })
//    ResponseDto<Void> createChat(
//            @PathVariable Long tutorId,
//            @AuthenticationPrincipal UserDetailsImpl userDetails);

    @Operation(summary = "참여 중인 채팅방 조회", description = "사용자가 참여 중이며 거절되지 않은 채팅방 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "참여 중인 채팅방 목록 반환 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (ErrorCode: A001 - 인증에 실패하였습니다)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음 (ErrorCode: A009 - 사용자의 권한을 찾을 수 없습니다)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "참여 중인 채팅방을 찾을 수 없음 (ErrorCode: CH001 - 존재하지 않는 채팅방입니다)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<List<ChatParticipatingResponseDto>> getParticipatingChats(
            @AuthenticationPrincipal UserDetailsImpl userDetails);

//    @Operation(summary = "튜터 요청 거절", description = "해당 채팅방의 튜터 요청을 거절합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "요청 거절 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode: C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
//            @ApiResponse(responseCode = "401", description = "인증 실패 (ErrorCode: A001 - 인증에 실패하였습니다)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
//            @ApiResponse(responseCode = "403", description = "접근 권한 없음 (ErrorCode: A009 - 사용자의 권한을 찾을 수 없습니다)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
//            @ApiResponse(responseCode = "404", description = "해당 채팅방 요청을 찾을 수 없음 (ErrorCode: CH001 - 존재하지 않는 채팅방입니다)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
//            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
//    })
//    ResponseDto<Void> rejectChatRequest(
//            @PathVariable Long roomId,
//            @AuthenticationPrincipal UserDetailsImpl userDetails);
}

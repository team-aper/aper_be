package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aper.web.domain.chat.dto.ChatParticipatingResponseDto;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "chat-controller", description = "to create Chat w. tutor")
public interface ChatControllerDocs {

    @Operation(summary = "튜터와 채팅방 만들기", description = "이미 생성된 채팅방인지 확인하고, 생성되어 있지 않다면 채팅방 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅방 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode: C002 - 유효성 검사 실패)"),
            @ApiResponse(responseCode = "401", description = "인증 실패 (ErrorCode: A001 - 인증에 실패하였습니다)"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음 (ErrorCode: A009 - 사용자의 권한을 찾을 수 없습니다)"),
            @ApiResponse(responseCode = "404", description = "튜터를 찾을 수 없음 (ErrorCode: CH001 - 존재하지 않는 튜터입니다)"),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)")
    })
    ResponseDto<Void> createChat(
            @PathVariable Long tutorId,
            @AuthenticationPrincipal UserDetailsImpl userDetails);

    @Operation(summary = "참여 중인 채팅방", description = "참여 중이고 거절되지 않은 채팅방 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "참여 중인 채팅방 반환 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패 (ErrorCode: A001 - 인증에 실패하였습니다)"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음 (ErrorCode: A009 - 사용자의 권한을 찾을 수 없습니다)"),
            @ApiResponse(responseCode = "404", description = "참여 중인 채팅방을 찾을 수 없음 (ErrorCode: CH001 - 존재하지 않는 튜터입니다)"),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)")
    })
    ResponseDto<List<ChatParticipatingResponseDto>> getParticipatingChats(
            @AuthenticationPrincipal UserDetailsImpl userDetails);

    @Operation(summary = "튜터 요청 거절", description = "요청 유무를 확인하고 유무에 따라 튜터 요청을 거절")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 거절 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode: C002 - 유효성 검사 실패)"),
            @ApiResponse(responseCode = "401", description = "인증 실패 (ErrorCode: A001 - 인증에 실패하였습니다)"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음 (ErrorCode: A009 - 사용자의 권한을 찾을 수 없습니다)"),
            @ApiResponse(responseCode = "404", description = "해당 채팅방 요청을 찾을 수 없음 (ErrorCode: CH001 - 존재하지 않는 튜터입니다)"),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)")
    })
    ResponseDto<Void> rejectChatRequest(
            @PathVariable Long roomId,
            @AuthenticationPrincipal UserDetailsImpl userDetails);
}

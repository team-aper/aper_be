package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
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
    ResponseDto<Void> createChat(
            @PathVariable Long tutorId,
            @AuthenticationPrincipal UserDetailsImpl userDetails);

    @Operation(summary = "참여 중인 채팅방", description = "참여 중이고 거절되지 않은 채팅방 반환")
    ResponseDto<List<ChatParticipatingResponseDto>> getParticipatingChats(
            @AuthenticationPrincipal UserDetailsImpl userDetails);

    @Operation(summary = "튜터 요청 거절", description = "요청 유무를 확인하고 유무에 따라 튜터 요청을 거절")
    ResponseDto<Void> rejectChatRequest(
            @PathVariable Long roomId,
            @AuthenticationPrincipal UserDetailsImpl userDetails);
}

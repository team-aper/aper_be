package org.aper.web.domain.chat.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aper.web.domain.chat.dto.ChatParticipatingResponseDto;
import org.aper.web.domain.chat.service.ChatService;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@Tag(name = "chat-controller", description = "to create Chat w. tutor")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }


    @PostMapping("/{tutorId}")
    @Operation(summary = "튜터와 채팅방 만들기", description = "이미 생성된 채팅방인지 확인하고, 생성되어 있지 않다면 채팅방 생성")
    public ResponseDto<Void> createChat(
            @PathVariable Long tutorId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.user().getUserId();
        if (chatService.isCreatedChat(userId, tutorId)) {
            return ResponseDto.fail("이미 생성된 채팅방 입니다.");
        }
        return chatService.createChat(userId, tutorId);
    }

    @GetMapping
    @Operation(summary = "참여 중인 채팅방", description = "참여 중이고 거절되지 않은 채팅방 반환")
    public ResponseDto<List<ChatParticipatingResponseDto>> getParticipatingChats(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.getParticipatingChats(userDetails.user().getUserId());
    }

    @DeleteMapping("/{roomId}")
    @Operation(summary = "튜터 요청 거절", description = "요청 유무를 확인하고 유무에 따라 튜터 요청을 거절")
    public ResponseDto<Void> rejectChatRequest(
            @PathVariable Long roomId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.rejectChatRoomRequest(roomId, userDetails.user().getUserId());
    }
}

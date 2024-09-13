package org.aper.web.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.chat.dto.ChatParticipatingResponseDto;
import org.aper.web.domain.chat.service.ChatService;
import org.aper.web.global.docs.ChatControllerDocs;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController implements ChatControllerDocs {

    private final ChatService chatService;

    @PostMapping("/{tutorId}")
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
    public ResponseDto<List<ChatParticipatingResponseDto>> getParticipatingChats(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.getParticipatingChats(userDetails.user().getUserId());
    }

    @DeleteMapping("/{roomId}")
    public ResponseDto<Void> rejectChatRequest(
            @PathVariable Long roomId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.rejectChatRoomRequest(roomId, userDetails.user().getUserId());
    }
}

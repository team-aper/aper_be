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
        chatService.createChat(userId, tutorId);
        return ResponseDto.success("성공적으로 채팅방을 생성하였습니다.");
    }

    @GetMapping
    public ResponseDto<List<ChatParticipatingResponseDto>> getParticipatingChats(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ChatParticipatingResponseDto> response = chatService.getParticipatingChats(userDetails.user().getUserId());
        return ResponseDto.success("성공적으로 채팅방을 찾았습니다", response);
    }

    @DeleteMapping("/{roomId}")
    public ResponseDto<Void> rejectChatRequest(
            @PathVariable Long roomId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatService.rejectChatRoomRequest(roomId, userDetails.user().getUserId());
        return ResponseDto.success("튜터 요청을 거절하였습니다.");
    }
}

package org.aper.web.domain.chat.controller;


import org.aper.web.domain.chat.service.ChatService;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }


    @PostMapping("/{tutorId}")
    public ResponseDto<Void> createChat(@PathVariable Long tutorId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.user().getUserId();
        if (chatService.isCreatedChat(userId, tutorId)) {
            return ResponseDto.fail("이미 생성된 채팅방 입니다.");
        }
        return chatService.createChat(userId, tutorId);
    }


}

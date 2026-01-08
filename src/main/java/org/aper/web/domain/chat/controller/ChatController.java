package org.aper.web.domain.chat.controller;

import org.aper.web.domain.chat.dto.ChatRequestDto.CreateChatRoomRequestDto;
import org.aper.web.domain.chat.dto.ChatResponseDto.ChatRoomResponseDto;
import org.aper.web.domain.chat.dto.ChatResponseDto.CreatedChatRoomResponseDto;
import org.aper.web.domain.chat.service.ChatRoomService;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/rooms")
    public ResponseDto<CreatedChatRoomResponseDto> createChatRoom(
            @RequestBody @Valid CreateChatRoomRequestDto request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CreatedChatRoomResponseDto response = chatRoomService.createChatRoom(request, userDetails.user().getUserId());
        return ResponseDto.success("채팅방 생성 성공", response);
    }

    @GetMapping("/rooms")
    public ResponseDto<List<ChatRoomResponseDto>> getChatRooms(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ChatRoomResponseDto> responses = chatRoomService.getChatRoomsForUser(userDetails.user().getUserId());
        return ResponseDto.success("채팅방 목록 조회 성공", responses);
    }

    @DeleteMapping("/rooms/{chatRoomId}")
    public ResponseDto<Void> deleteChatRoom(@PathVariable("chatRoomId") Long chatRoomId) {
        chatRoomService.deleteChatRoom(chatRoomId);
        return ResponseDto.success("채팅방 삭제 성공");
    }
}

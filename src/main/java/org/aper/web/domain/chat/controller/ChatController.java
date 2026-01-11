package org.aper.web.domain.chat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aper.web.domain.chat.dto.ChatRequestDto.CreateChatRoomRequestDto;
import org.aper.web.domain.chat.dto.ChatResponseDto;
import org.aper.web.domain.chat.dto.ChatResponseDto.ChatRoomResponseDto;
import org.aper.web.domain.chat.dto.ChatResponseDto.CreatedChatRoomResponseDto;
import org.aper.web.domain.chat.service.ChatMessageService;
import org.aper.web.domain.chat.service.ChatRoomService;
import org.aper.web.domain.user.entity.User;
import org.aper.web.global.annotation.CurrentUser;
import org.aper.web.global.dto.ResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    /**
     * 채팅방 생성
     */
    @PostMapping("/rooms")
    public ResponseDto<CreatedChatRoomResponseDto> createChatRoom(
            @RequestBody @Valid CreateChatRoomRequestDto request,
            @CurrentUser User user
    ) {
        CreatedChatRoomResponseDto response = chatRoomService.createChatRoom(request, user.getUserId());
        return ResponseDto.success("채팅방 생성 성공", response);
    }

    /**
     * 채팅방 목록 조회
     */
    @GetMapping("/rooms")
    public ResponseDto<Slice<ChatRoomResponseDto>> getChatRooms(
            @CurrentUser User user,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Slice<ChatRoomResponseDto> responses = chatRoomService.getChatRoomsForUser(user.getUserId(), pageable);
        return ResponseDto.success("채팅방 목록 조회 성공", responses);
    }

    /**
     * 채팅방 삭제
     */
    @DeleteMapping("/rooms/{chatRoomId}")
    public ResponseDto<Void> deleteChatRoom(@PathVariable("chatRoomId") Long chatRoomId) {
        chatRoomService.deleteChatRoom(chatRoomId);
        return ResponseDto.success("채팅방 삭제 성공");
    }

    /**
     * 채팅방의 메시지 목록 조회
     */
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseDto<Slice<ChatResponseDto.MessageDocumentResponseDto>> getMessages(
            @PathVariable Long roomId,
            @CurrentUser User user,
            @PageableDefault(size = 50) Pageable pageable
    ) {
        Slice<ChatResponseDto.MessageDocumentResponseDto> messages =
                chatMessageService.getMessages(roomId, user.getUserId(), pageable);
        return ResponseDto.success("메시지 조회 성공", messages);
    }

    /**
     * 특정 시간 이후 메시지 조회 (실시간 동기화용)
     */
    @GetMapping("/rooms/{roomId}/messages/since")
    public ResponseDto<List<ChatResponseDto.MessageDocumentResponseDto>> getMessagesSince(
            @PathVariable Long roomId,
            @RequestParam String since,
            @CurrentUser User user
    ) {
        LocalDateTime sinceTime = LocalDateTime.parse(since);
        List<ChatResponseDto.MessageDocumentResponseDto> messages =
                chatMessageService.getMessagesSince(roomId, user.getUserId(), sinceTime);
        return ResponseDto.success("최근 메시지 조회 성공", messages);
    }
}
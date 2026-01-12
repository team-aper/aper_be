package org.aper.web.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.chat.dto.ChatWebSocketDto.*;
import org.aper.web.domain.chat.service.ChatMessageService;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    /**
     * 메시지 전송
     * Client → /app/chat/{roomId}/send
     * Server → /topic/chatroom/{roomId} 브로드캐스트
     */
    @MessageMapping("/chat/{roomId}/send")
    public void sendMessage(
            @DestinationVariable Long roomId,
            @Payload SendMessageRequest request,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        Long userId = extractUserId(headerAccessor);

        log.info("Message received - roomId: {}, userId: {}, content: {}",
                roomId, userId, request.content());

        chatMessageService.sendMessage(roomId, userId, request);
    }

    /**
     * 읽음 처리
     * Client → /app/chat/{roomId}/read
     */
    @MessageMapping("/chat/{roomId}/read")
    public void markAsRead(
            @DestinationVariable Long roomId,
            @Payload MarkAsReadRequest request,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        Long userId = extractUserId(headerAccessor);

        log.info("Mark as read - roomId: {}, userId: {}, lastMessageId: {}", roomId, userId, request.lastReadMessageId());

        chatMessageService.markAsRead(roomId, userId, request);
    }

    /**
     * 수업 승인/거절
     * Client → /app/chat/{roomId}/lesson
     */
    @MessageMapping("/chat/{roomId}/lesson")
    public void handleLessonAction(
            @DestinationVariable Long roomId,
            @Payload LessonActionRequest request,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        Long userId = extractUserId(headerAccessor);

        log.info("Lesson action - roomId: {}, userId: {}, action: {}",
                roomId, userId, request.action());

        chatMessageService.handleLessonAction(roomId, userId, request.action());
    }


    private Long extractUserId(SimpMessageHeaderAccessor headerAccessor) {
        Object userIdObj = headerAccessor.getSessionAttributes().get("userId");
        if (userIdObj == null) {
            log.error("UserId not found in WebSocket session");
            throw new ServiceException(ErrorCode.UNAUTHORIZED_USER);
        }
        return Long.parseLong(userIdObj.toString());
    }
}
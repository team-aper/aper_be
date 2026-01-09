package org.aper.web.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.chat.dto.ChatWebSocketDto;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/{roomId}/send")
    public void sendMessage(
            @DestinationVariable Long roomId,
            @Payload ChatWebSocketDto.SendMessageRequest request,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        // TODO: 이거 말고 더 좋은 방법은 없나.
        Long userId = extractUserId(headerAccessor);

        log.info("Message received - roomId: {}, userId: {}, content: {}",
                roomId, userId, request.content());

        chatMessageService.sendMessage(roomId, userId, request);
    }


}

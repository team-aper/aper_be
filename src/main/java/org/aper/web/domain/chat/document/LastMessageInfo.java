package org.aper.web.domain.chat.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aper.web.domain.chat.dto.ChatWebSocketDto;
import org.aper.web.domain.user.entity.User;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LastMessageInfo {
    private String messageId;
    private Long senderId;
    private String senderName;
    private String senderImage;
    private String content;
    private LocalDateTime createdAt;

    public LastMessageInfo(String documentId, User user, ChatWebSocketDto.SendMessageRequest request) {
        LastMessageInfo.builder()
                .messageId(documentId)
                .senderId(user.getUserId())
                .senderName(user.getPenName())
                .senderImage(user.getFieldImage())
                .content(request.content())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
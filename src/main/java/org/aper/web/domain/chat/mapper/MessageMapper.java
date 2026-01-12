package org.aper.web.domain.chat.mapper;

import org.aper.web.domain.chat.document.MessageDocument;
import org.aper.web.domain.chat.dto.ChatResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 메시지 DTO 변환
 */
@Component
public class MessageMapper {

    /**
     * MessageDocument → DTO
     */
    public ChatResponseDto.MessageDocumentResponseDto toDto(MessageDocument msg) {
        return new ChatResponseDto.MessageDocumentResponseDto(
                msg.getId(),
                msg.getChatRoomId(),
                new ChatResponseDto.MessageSenderInfo(
                        msg.getSenderId(),
                        msg.getSenderName(),
                        msg.getSenderImage()
                ),
                msg.getContent(),
                msg.getType(),
                msg.getPayload(),
                msg.getMessageSequence(),
                msg.getCreatedAt()
        );
    }

    /**
     * List<MessageDocument> → List<DTO>
     */
    public List<ChatResponseDto.MessageDocumentResponseDto> toDtoList(List<MessageDocument> messages) {
        return messages.stream()
                .map(this::toDto)
                .toList();
    }
}

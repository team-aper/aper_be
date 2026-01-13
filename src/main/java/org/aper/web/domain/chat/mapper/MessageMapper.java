package org.aper.web.domain.chat.mapper;

import org.aper.web.domain.chat.document.MessageDocument;
import org.aper.web.domain.chat.dto.response.MessageResponse;
import org.aper.web.domain.chat.dto.response.common.SenderInfo;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class MessageMapper {

    public MessageResponse.MessageDocumentDto toDto(MessageDocument msg) {
        return new MessageResponse.MessageDocumentDto(
                msg.getId(),
                msg.getChatRoomId(),
                new SenderInfo(
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

    public List<MessageResponse.MessageDocumentDto> toDtoList(List<MessageDocument> messages) {
        return messages.stream()
                .map(this::toDto)
                .toList();
    }
}

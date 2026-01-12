package org.aper.web.domain.chat.service;

import lombok.Getter;
import org.aper.web.domain.chat.document.ChatRoomSummaryDocument;
import org.aper.web.domain.chat.document.MessageDocument;
import org.aper.web.domain.chat.dto.ChatWebSocketDto;
import org.aper.web.domain.chat.entity.ChatRoom;
import org.aper.web.domain.user.entity.User;

@Getter
public class MessageContext {
    private final Long roomId;
    private final ChatRoom chatRoom;
    private final User sender;
    private final ChatWebSocketDto.SendMessageRequest request;

    private final MessageDocument message;
    private final ChatRoomSummaryDocument summary;
    private final Long sequence;

    public MessageContext(ChatRoom chatRoom,
                          User sender,
                          ChatWebSocketDto.SendMessageRequest request,
                          ChatRoomSummaryDocument summary,
                          MessageDocument message,
                          Long sequence) {
        this.roomId = chatRoom.getId();
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.request = request;
        this.summary = summary;
        this.message = message;
        this.sequence = sequence;
    }
}
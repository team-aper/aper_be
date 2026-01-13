package org.aper.web.domain.chat.mapper;

import org.aper.web.domain.chat.document.LastMessageInfo;
import org.aper.web.domain.chat.dto.response.ChatRoomResponse;
import org.aper.web.domain.chat.entity.ChatRoom;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomMapper {
    public ChatRoomResponse.ChatRoomSummaryDto toSummaryDto(ChatRoom room, LastMessageInfo lastMessage, Integer unreadCount) {
        return ChatRoomResponse.ChatRoomSummaryDto.fromMongo(room, lastMessage, unreadCount);
    }
}

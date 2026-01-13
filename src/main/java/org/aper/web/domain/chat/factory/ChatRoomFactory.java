package org.aper.web.domain.chat.factory;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.chat.document.ChatRoomSummaryDocument;
import org.aper.web.domain.chat.dto.ChatRequestDto.CreateChatRoomRequestDto;
import org.aper.web.domain.chat.dto.response.ChatRoomResponse.ChatRoomSummaryDto;
import org.aper.web.domain.chat.entity.ChatRoom;
import org.aper.web.domain.chat.entity.ChatRoomMember;
import org.aper.web.domain.chat.entity.constant.ChatRoomType;
import org.aper.web.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomFactory {


    public ChatRoom create(CreateChatRoomRequestDto request) {
        int memberCount = request.memberIds().size() + 1;

        return ChatRoom.builder()
                .name(request.name())
                .type(ChatRoomType.GROUP)
                .memberCount(memberCount)
                .build();
    }

    public ChatRoomMember createMember(ChatRoom chatRoom, User user, boolean isOwner) {
        return ChatRoomMember.create(chatRoom, user, isOwner);
    }

    public ChatRoomSummaryDto response(ChatRoom room,
                                       ChatRoomSummaryDocument summary,
                                       Integer unreadCount) {
        return ChatRoomSummaryDto.fromMongo(
                room,
                summary != null ? summary.getLastMessage() : null,
                unreadCount != null ? unreadCount : 0);
    }
}

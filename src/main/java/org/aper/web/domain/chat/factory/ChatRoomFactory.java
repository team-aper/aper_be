package org.aper.web.domain.chat.factory;

import org.aper.web.domain.chat.policy.ChatRoomPolicy;
import org.aper.web.domain.chat.dto.ChatRequestDto.CreateChatRoomRequestDto;
import org.aper.web.entity.chat.ChatRoom;
import org.aper.web.entity.chat.constant.ChatRoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatRoomFactory {

    private final ChatRoomPolicy chatRoomPolicy;

    public ChatRoom create(CreateChatRoomRequestDto request) {
        chatRoomPolicy.validateRoomName(request.name());

        int memberCount = request.memberIds().size() + 1;
        chatRoomPolicy.validateMemberCount(memberCount);

        return ChatRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .name(request.name())
                .type(ChatRoomType.GROUP)
                .memberCount(memberCount)
                .build();
    }
}

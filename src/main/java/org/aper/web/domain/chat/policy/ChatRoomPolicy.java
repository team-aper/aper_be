package org.aper.web.domain.chat.policy;

import org.aper.web.domain.chat.entity.ChatRoom;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomPolicy {

    private final ChatRoomRepository chatRoomRepository;

    private static final int MAX_MEMBERS = 100;

    public void validateRoomName(String name) {
        if (name == null || name.isBlank()) {
            throw new ServiceException(ErrorCode.CHAT_ROOM_NAME_REQUIRED);
        }
    }

    public void validateMemberCount(int count) {
        if (count > MAX_MEMBERS) {
            throw new ServiceException(ErrorCode.CHAT_ROOM_MEMBER_LIMIT_EXCEEDED);
        }
    }

    public ChatRoom validateChatRoomExists(Long chatRoomId) {
        return chatRoomRepository.getChatRoomById(chatRoomId).orElseThrow(
                () -> new ServiceException(ErrorCode.CHAT_ROOM_NOT_FOUND)
        );
    }
}

package org.aper.web.domain.chat.validator;

import org.aper.web.domain.chat.dto.ChatRequestDto;
import org.aper.web.domain.chat.entity.ChatRoom;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomValidator {

    private static final int MAX_MEMBERS = 100;
    private final ChatRoomRepository chatRoomRepository;

    public void validateCreate(ChatRequestDto.CreateChatRoomRequestDto request) {
        validateRoomName(request.name());
        validateMemberCount(request.memberIds().size() + 1);
    }

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

package org.aper.web.domain.chat.validator;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.aper.web.domain.chat.entity.ChatRoom;
import org.aper.web.domain.chat.repository.ChatRoomMemberRepository;
import org.aper.web.domain.chat.repository.ChatRoomRepository;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.stereotype.Component;

/**
 * 채팅방 접근 권한 검증
 */
@Component
@RequiredArgsConstructor
public class ChatRoomAccessValidator {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UserRepository userRepository;

    public ValidationResult validate(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ServiceException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        chatRoomMemberRepository.findByChatRoomAndUserUserId(chatRoom, userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_A_CHATROOM_MEMBER));

        return new ValidationResult(chatRoom, user);
    }

    @Value
    public static class ValidationResult {
        ChatRoom chatRoom;
        User user;
    }
}

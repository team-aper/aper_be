package org.aper.web.domain.chat.query;

import org.aper.web.entity.chat.Message;
import org.aper.web.entity.chat.UserReadTracking;
import org.aper.web.domain.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnreadCountCalculator {
    private final MessageRepository messageRepository;

    public Integer calculate(Long chatRoomId,
                             UserReadTracking readTracking,
                             Message lastMessage) {
        if (readTracking == null) {
            return messageRepository.countByChatRoomId(chatRoomId);
        }

        if (lastMessage == null) {
            return 0;
        }

        if (readTracking.getLastReadMessage() == null) {
            return messageRepository.countByChatRoomId(chatRoomId);
        }

        Long lastReadMessageId = readTracking.getLastReadMessage().getId();
        return messageRepository.countUnreadMessages(chatRoomId, lastReadMessageId);
    }
}

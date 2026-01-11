package org.aper.web.domain.chat.query;

import org.aper.web.domain.chat.entity.Message;
import org.aper.web.domain.chat.entity.UserReadTracking;
import org.aper.web.domain.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnreadCountCalculator {
    private final MessageRepository messageRepository;

    // TODO: 추후에 Batch 변경으로 N+1 문제 해결
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

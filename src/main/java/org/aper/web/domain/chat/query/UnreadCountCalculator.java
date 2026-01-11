package org.aper.web.domain.chat.query;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.chat.document.MessageDocument;
import org.aper.web.domain.chat.document.UserReadTrackingDocument;
import org.aper.web.domain.chat.repository.MessageDocumentRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UnreadCountCalculator {
    private final MessageDocumentRepository messageDocumentRepository;

    /**
     * 단일 채팅방의 안읽은 메시지 수 계산
     */
    public Integer calculate(Long chatRoomId,
                             UserReadTrackingDocument tracking,
                             MessageDocument lastMessage) {
        if (lastMessage == null) {
            return 0;
        }

        if (tracking == null || tracking.getLastReadSequence() == null) {
            // 한 번도 읽지 않은 경우: 전체 메시지 개수
            return messageDocumentRepository.countByChatRoomIdAndIsDeletedFalse(chatRoomId).intValue();
        }

        // 마지막 읽은 시퀀스 이후의 메시지 개수
        return messageDocumentRepository
                .countUnreadMessages(chatRoomId, tracking.getLastReadSequence())
                .intValue();
    }

    /**
     * Batch로 여러 채팅방의 안읽은 메시지 수 계산
     */
    public Map<Long, Integer> calculateBatch(
            Long userId,
            List<Long> chatRoomIds,
            Map<Long, UserReadTrackingDocument> trackingMap,
            Map<Long, MessageDocument> latestMessageMap
    ) {
        Map<Long, Integer> result = new HashMap<>();

        for (Long chatRoomId : chatRoomIds) {
            UserReadTrackingDocument tracking = trackingMap.get(chatRoomId);
            MessageDocument lastMessage = latestMessageMap.get(chatRoomId);

            Integer unreadCount = calculate(chatRoomId, tracking, lastMessage);
            result.put(chatRoomId, unreadCount);
        }

        return result;
    }
}

package org.aper.web.domain.chat.document;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aper.web.domain.chat.entity.ChatRoom;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * 채팅방 목록 조회 최적화를 위한 요약 정보
 * MySQL ChatRoom과 동기화됨
 */
@Document(collection = "chat_room_summaries")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomSummaryDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field("chatRoomId")
    private Long chatRoomId;  // MySQL chat_rooms.id

    @Field("lastMessage")
    private LastMessageInfo lastMessage;

    @Field("currentSequence")
    @Builder.Default
    private Long currentSequence = 0L;

    @LastModifiedDate
    @Field("updatedAt")
    private LocalDateTime updatedAt;


    public ChatRoomSummaryDocument(ChatRoom chatRoom) {
        ChatRoomSummaryDocument.builder()
                .chatRoomId(chatRoom.getId())
                .currentSequence(0L)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public ChatRoomSummaryDocument(Long roomId) {
        ChatRoomSummaryDocument.builder()
                .chatRoomId(roomId)
                .currentSequence(0L)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
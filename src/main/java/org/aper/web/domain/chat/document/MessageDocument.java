package org.aper.web.domain.chat.document;


import lombok.Builder;
import lombok.Data;
import org.aper.web.domain.chat.entity.constant.MessageType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "messages")
@CompoundIndex(
        name = "room_created_idx",
        def = "{'chatRoomId':1, 'createdAt':-1, '_id' : -1}"
)
@Data
@Builder
public class MessageDocument {
    @Id
    private String id;
    private Long chatRoomId;

    private Long senderId;
    private String senderName;
    private String senderImage;

    private String content;
    private MessageType type;

    private Boolean isDeleted;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Map<String, Object> payload;

}

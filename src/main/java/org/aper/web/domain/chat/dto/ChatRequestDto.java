package org.aper.web.domain.chat.dto;

import org.aper.web.entity.chat.constant.ChatRoomType;
import org.aper.web.entity.chat.constant.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ChatRequestDto {

    public record CreateChatRoomRequestDto(
            @NotBlank(message = "Room name is required")
            String name,

            @NotNull(message = "Room type is required")
            ChatRoomType type,

            @NotEmpty(message = "At least one member is required")
            List<Long> memberIds
    ) {}

    public record SendMessageRequestDto(
            @NotBlank(message = "Content is required")
            String content,

            @NotNull(message = "Message type is required")
            MessageType type,

            String fileUrl
    ) {}

    public record MarkAsReadRequestDto(
            @NotNull(message = "Last read message ID is required")
            Long lastReadMessageId
    ) {}
}

package org.aper.web.domain.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.aper.web.domain.chat.entity.constant.ChatRoomType;
import org.aper.web.domain.chat.entity.constant.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ChatRequestDto {

    public record CreateChatRoomRequestDto(
            @Schema(description = "chat Room name", nullable = true)
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

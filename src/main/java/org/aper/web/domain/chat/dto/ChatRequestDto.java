package org.aper.web.domain.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.aper.web.domain.chat.entity.constant.ChatRoomType;

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

}

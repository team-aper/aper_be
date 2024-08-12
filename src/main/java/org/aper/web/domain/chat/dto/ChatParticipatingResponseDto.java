package org.aper.web.domain.chat.dto;

import java.time.LocalDateTime;

public record ChatParticipatingResponseDto (
    Long chatRoomId,
    Boolean isTutor,
    Boolean isAccepted,
    LocalDateTime startTime

) {
}

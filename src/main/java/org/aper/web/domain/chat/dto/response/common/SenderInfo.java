package org.aper.web.domain.chat.dto.response.common;


public record SenderInfo(
        Long userId,
        String nickname,
        String profileImage
) {
}

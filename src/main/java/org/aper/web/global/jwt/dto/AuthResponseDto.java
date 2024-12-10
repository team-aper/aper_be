package org.aper.web.global.jwt.dto;

public class AuthResponseDto {

    public record UserInfo (
            Long id,
            String email,
            String penName,
            String fieldImage
    ) {}
}

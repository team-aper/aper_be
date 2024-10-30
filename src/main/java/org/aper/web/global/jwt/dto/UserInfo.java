package org.aper.web.global.jwt.dto;


public record UserInfo (
        Long id,
        String email,
        String penName,
        String fieldImage
) {}

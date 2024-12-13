package org.aper.web.global.jwt.dto;

public class AuthRequestDto {

    public record GetMeRequestDto(
         String tempToken
    ){}
}

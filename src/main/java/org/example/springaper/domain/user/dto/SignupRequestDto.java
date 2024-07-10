package org.example.springaper.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    @NotBlank
    private String penName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
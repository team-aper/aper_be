package org.aper.web.domain.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.user.dto.UserRequestDto.EmailAuthDto;
import org.aper.web.domain.user.dto.UserRequestDto.EmailSendDto;
import org.aper.web.domain.user.dto.UserRequestDto.PasswordChangeDto;
import org.aper.web.domain.user.dto.UserRequestDto.SignupRequestDto;
import org.aper.web.domain.user.dto.UserResponseDto.SignupResponseDto;
import org.aper.web.domain.user.service.EmailCertService;
import org.aper.web.domain.user.service.PasswordService;
import org.aper.web.domain.user.service.UserService;
import org.aper.web.domain.user.valid.UserValidationSequence;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Validated(UserValidationSequence.class)
@Tag(name = "User", description = "회원 API")
public class UserController {

    private final UserService userService;
    private final EmailCertService emailCertService;
    private final PasswordService passwordService;

    public UserController(UserService userService, EmailCertService emailCertService, PasswordService passwordService) {
        this.userService = userService;
        this.emailCertService = emailCertService;
        this.passwordService = passwordService;
    }

    @PostMapping("/signup")
    public ResponseDto<SignupResponseDto> signup(@RequestBody @Valid SignupRequestDto requestDto) {
        return ResponseDto.success("회원가입 성공", userService.signupUser(requestDto));
    }

    @PostMapping("/email/send")
    public ResponseDto<Void> emailSend(@RequestBody @Valid EmailSendDto emailSendRequestDto) {
        emailCertService.emailSend(emailSendRequestDto);
        return ResponseDto.success("인증메일 전송에 성공하였습니다.");
    }

    @PostMapping("/email/auth")
    public ResponseDto<Void> emailAuthCheck(@RequestBody @Valid EmailAuthDto emailAuthDto) {
        emailCertService.emailAuthCheck(emailAuthDto);
        return ResponseDto.success("이메일 인증에 성공하였습니다.");
    }

    @PutMapping("/password/change")
    public ResponseDto<Void> passwordChange(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @RequestBody PasswordChangeDto passChangeDto) {
        passwordService.changePassword(userDetails.user(), passChangeDto);
        return ResponseDto.success("비밀번호 변경에 성공하였습니다.");
    }
}
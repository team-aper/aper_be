package org.aper.web.domain.user.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.user.docs.UserControllerDocs;
import org.aper.web.domain.user.dto.UserRequestDto;
import org.aper.web.domain.user.dto.UserRequestDto.*;
import org.aper.web.domain.user.dto.UserResponseDto.*;
import org.aper.web.domain.user.service.EmailCertService;
import org.aper.web.domain.user.service.PasswordService;
import org.aper.web.domain.user.service.UserService;
import org.aper.web.domain.user.valid.UserValidationSequence;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@Validated(UserValidationSequence.class)
public class UserController implements UserControllerDocs {

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

    @Override
    @PutMapping("/penname/change")
    public ResponseDto<Void> changePenName(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid ChangePenNameDto changePenNameDto
    ) {
        userService.ChangePenName(userDetails.user(), changePenNameDto);
        return ResponseDto.success("필명 변경에 성공하였습니다.");
    }

    @Override
    @PutMapping("/email/change")
    public ResponseDto<Void> changeEmail(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid UserRequestDto.ChangeEmailDto changeEmailDto
    ) {
        userService.changeEmail(userDetails.user(), changeEmailDto);
        return ResponseDto.success("이메일 변경에 성공하였습니다.");
    }

    @Override
    @PutMapping("/description/change")
    public ResponseDto<Void> changeDescription(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody ChangeDescriptionDto descriptionDto
    ) {
        userService.changeDescription(userDetails.user(), descriptionDto);
        return ResponseDto.success("작가의 말 변경에 성공하였습니다.");
    }

    @Override
    @PutMapping("/image")
    public ResponseDto<String> changeImage(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("fieldImageFile") MultipartFile fieldImageFile
    ) {
        String imageUrl = userService.changeImage(userDetails.user(), fieldImageFile);
        return ResponseDto.success("필드 이미지 업로드 완료", imageUrl);
    }

    public ResponseDto<Void> deleteAccount(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody DeletePasswordDto deletePasswordDto
    ) {
        userService.deleteAccount(userDetails.user(), deletePasswordDto);
        return ResponseDto.success("계정 탈퇴에 성공하였습니다.");
    }
}

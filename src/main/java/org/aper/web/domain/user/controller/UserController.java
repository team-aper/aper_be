package org.aper.web.domain.user.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.user.dto.UserRequestDto.*;
import org.aper.web.domain.user.dto.UserResponseDto.CreatedReviewDto;
import org.aper.web.domain.user.dto.UserResponseDto.IsDuplicated;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.service.*;
import org.aper.web.domain.user.valid.UserValidationSequence;
import org.aper.web.global.docs.UserControllerDocs;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@Validated(UserValidationSequence.class)
public class UserController implements UserControllerDocs {

    private final UserService userService;
    private final EmailCertService emailCertService;
    private final PasswordService passwordService;
    private final DeleteService deleteService;

    public UserController(UserService userService,
                          EmailCertService emailCertService,
                          PasswordService passwordService,
                          DeleteService deleteService) {
        this.userService = userService;
        this.emailCertService = emailCertService;
        this.passwordService = passwordService;
        this.deleteService = deleteService;
    }

    @PostMapping("/signup")
    public ResponseDto<Void> signup(@RequestBody @Valid SignupRequestDto requestDto) {
        userService.signupUser(requestDto);
        return ResponseDto.success("회원가입 성공");
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

    @GetMapping("/email/check")
    public ResponseDto<IsDuplicated> emailCheck(@RequestParam String email) {
        return ResponseDto.success("이메일 중복 체크 데이터", userService.emailCheck(email));
    }

    @PutMapping("/password")
    public ResponseDto<Void> passwordChange(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @RequestBody PasswordChangeDto passChangeDto) {
        passwordService.changePassword(userDetails.user(), passChangeDto);
        return ResponseDto.success("비밀번호 변경에 성공하였습니다.");
    }

//    @Override
//    @PutMapping("/penname")
//    public ResponseDto<Void> changePenName(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
//            @RequestBody @Valid ChangePenNameDto changePenNameDto
//    ) {
//        userService.ChangePenName(userDetails.user(), changePenNameDto);
//        return ResponseDto.success("필명 변경에 성공하였습니다.");
//    }

    @Override
    @PutMapping("/email")
    public ResponseDto<Void> changeEmail(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid ChangeEmailDto changeEmailDto
    ) {
        userService.changeEmail(userDetails.user(), changeEmailDto);
        return ResponseDto.success("이메일 변경에 성공하였습니다.");
    }

//    @Override
//    @PutMapping("/description")
//    public ResponseDto<Void> changeDescription(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
//            @RequestBody ChangeDescriptionDto descriptionDto
//    ) {
//        userService.changeDescription(userDetails.user(), descriptionDto);
//        return ResponseDto.success("작가의 말 변경에 성공하였습니다.");
//    }

//    @Override
//    @PutMapping("/image")
//    public ResponseDto<String> changeImage(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
//            @RequestParam("fieldImageFile") MultipartFile fieldImageFile
//    ) {
//        String imageUrl = userService.changeImage(userDetails.user(), fieldImageFile);
//        return ResponseDto.success("필드 이미지 업로드 완료", imageUrl);
//    }

//    @Override
//    @PutMapping("/history")
//    public ResponseDto<Void> changeHistory(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
//            @RequestBody List<HistoryRequestDto> historyDtoList
//    ) {
//        userHistoryService.changeHistory(userDetails.user(), historyDtoList);
//        return ResponseDto.success("작가 이력 작성 완료.");
//    }

//    @Override
//    @PutMapping("/contact/email")
//    public ResponseDto<Void> changeContactEmail(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
//            @RequestBody @Valid ChangeEmailDto changeEmailDto
//    ) {
//        userService.changeContactEmail(userDetails.user(), changeEmailDto);
//        return ResponseDto.success("컨택 이메일 작성에 성공하였습니다.");
//    }

//    @Override
//    @PutMapping("/class/description")
//    public ResponseDto<Void> changeClassDescription(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
//            @RequestBody @Valid ClassDescriptionRequestDto requestDto
//    ) {
//        userService.changeClassDescription(userDetails.user(), requestDto);
//        return ResponseDto.success("1:1 수업 소개 변경에 성공하였습니다.");
//    }

    @Override
    @PostMapping("/verify")
    public ResponseDto<Void> verifyPassword(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody PasswordVerifyDto password
    ) {
        passwordService.verifyPassword(userDetails.user().getPassword(), password.password());
        return ResponseDto.success("비밀번호가 일치합니다.");
    }

    @Override
    @DeleteMapping("/account")
    public ResponseDto<Void> deleteAccount(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        deleteService.deleteAccount(userDetails.user());
        return ResponseDto.success("계정 탈퇴에 성공하였습니다.");
    }

    @Override
    @DeleteMapping("/scheduler/test")
    public ResponseDto<Void> deleteAccountScheduler() {
        deleteService.deleteAccountScheduler();
        return ResponseDto.success("계정 탈퇴 스케쥴러 작동 완료");
    }

    @PostMapping("/review")
    public ResponseDto<CreatedReviewDto> createReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid CreateReviewRequestDto requestDto) {
        User reviewer = userDetails.user();
        CreatedReviewDto reviewData = userService.createReview(reviewer, requestDto);
        return ResponseDto.success("리뷰작성에 성공하였습니다.", reviewData);
    }
}

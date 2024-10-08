package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.aper.web.domain.user.dto.UserRequestDto.*;
import org.aper.web.domain.user.dto.UserResponseDto;
import org.aper.web.domain.user.dto.UserResponseDto.SignupResponseDto;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "User", description = "회원 API")
public interface UserControllerDocs {

    @Operation(summary = "회원가입 API", description = "회원가입을 위한 API입니다. 이메일, 비밀번호, 닉네임 등을 입력하여 요청을 보냅니다.")
    ResponseDto<SignupResponseDto> signup(
            @RequestBody @Valid SignupRequestDto requestDto
    );

    @Operation(summary = "이메일 인증 메일 전송 API", description = "회원가입 시 입력한 이메일로 인증 메일을 전송합니다.")
    ResponseDto<Void> emailSend(
            @RequestBody @Valid EmailSendDto emailSendRequestDto
    );

    @Operation(summary = "이메일 인증 확인 API", description = "전송된 이메일의 인증 코드를 입력하여 이메일 인증을 완료합니다.")
    ResponseDto<Void> emailAuthCheck(
            @RequestBody @Valid EmailAuthDto emailAuthDto
    );

    @Operation(summary = "비밀번호 변경 API", description = "로그인한 사용자가 본인의 비밀번호를 변경합니다.")
    ResponseDto<Void> passwordChange(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody PasswordChangeDto passChangeDto
    );

    @Operation(summary = "필명 수정 API", description = "공백과 특수문자를 사용하지 않고 2~10글자 사이로 필명을 수정합니다.")
    ResponseDto<Void> changePenName(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid ChangePenNameDto changePenNameDto
    );

    @Operation(summary = "이메일 수정 API", description = "공백 없이 이메일 형식을 지켜서 이메일을 수정합니다.")
    ResponseDto<Void> changeEmail(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid ChangeEmailDto changeEmailDto
    );

    @Operation(summary = "작가 소개글 수정 API", description = "사용자가 자신의 소개글을 수정합니다.")
    ResponseDto<Void> changeDescription(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody ChangeDescriptionDto descriptionDto
    );

    @Operation(summary = "작가 필드 이미지 수정 API", description = "작가의 프로필 이미지를 수정합니다. form-data 형식으로 이미지 파일을 전송해야 합니다.")
    ResponseDto<String> changeImage(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("fieldImageFile") MultipartFile fieldImageFile
    );

    @Operation(summary = "작가 이력 생성, 수정 API",
            description = "작가의 이력(학력, 수상, 출간)을 생성 및 수정 합니다.<br>" +
                    "학력, 수상, 출간 각각 requestBody 담길 historyType으로 분류하여 처리하고 각각 EDUCATION, AWARD, PUBLICATION 으로 구분됩니다.<br>" +
                    "requestBody의 historyId를 null로 보내주면 생성, null이 아니라 값이 존재한다면 수정으로 처리됩니다.<br>" +
                    "수상, 출간 기록의 경우 requestBody의 endDate를 null로, 학력의 경우 값을 넣어 보내주면 됩니다.<br>" +
                    "이력의 종류와 상관없이 리스트 형태로 요청과 응답이 이루어집니다.<br>" +
                    "<h4> 종류와 상관없이 생성, 수정 모두 date, description은 필수 입력값입니다.<h4>"
    )
    ResponseDto<Void> changeEducation(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody List<HistoryRequestDto> historyDtoList
    );

    @Operation(summary = "컨택 메일 수정 API", description = "공백 없이 이메일 형식을 지켜서 컨택 이메일을 수정합니다.")
    ResponseDto<Void> changeContactEmail(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid ChangeEmailDto changeEmailDto
    );

    @Operation(summary = "1:1 수업 소개 수정 API", description = "2000자까지 입력 가능한 수업 소개 내용입니다.")
    ResponseDto<Void> changeClassDescription(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid ClassDescriptionRequestDto requestDto
    );

    @Operation(summary = "계정 탈퇴 API", description = "계정 삭제 요청을 통해 계정 상태를 삭제 상태로 변경하고, 이후 일주일 후 작성한 모든 콘텐츠가 삭제됩니다.")
    ResponseDto<Void> deleteAccount(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody DeletePasswordDto deletePasswordDto
    );

    @Operation(summary = "계정 탈퇴 싱크 맞추기 개발용 API 테스트", description = "계정 탈퇴 스케쥴러 실행하는 개발용 테스트 API")
    ResponseDto<Void> deleteAccountScheduler();
}

package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.aper.web.domain.user.dto.UserRequestDto.*;
import org.aper.web.domain.user.dto.UserResponseDto.*;
import org.aper.web.global.dto.ErrorResponseDto;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "User", description = "회원 API")
public interface UserControllerDocs {

    @Operation(summary = "회원가입 API", description = "회원가입을 위한 API입니다. 이메일, 비밀번호, 닉네임 등을 입력하여 요청을 보냅니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "U001 - 이미 가입된 이메일입니다,\n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<Void> signup(@RequestBody @Valid SignupRequestDto requestDto);

    @Operation(summary = "이메일 인증 메일 전송 API", description = "회원가입 시 입력한 이메일로 인증 메일을 전송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증메일 전송 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "U002 - 등록되지 않은 회원입니다,\n" +
                    "U003 - 인증코드 전송에 실패하였습니다,\n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<Void> emailSend(@RequestBody @Valid EmailSendDto emailSendRequestDto);

    @Operation(summary = "이메일 인증 확인 API", description = "전송된 이메일의 인증 코드를 입력하여 이메일 인증을 완료합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 인증 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "U004 - 이메일 인증에 실패하였습니다,\n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<Void> emailAuthCheck(@RequestBody @Valid EmailAuthDto emailAuthDto);

    @Operation(summary = "이메일 중복 체크 API", description = "입력한 이메일이 이미 사용 중인지 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "중복되지 않은 이메일", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<IsDuplicated> emailCheck(@RequestParam String email);

    @Operation(summary = "비밀번호 변경 API", description = "로그인한 사용자가 본인의 비밀번호를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "U005 - 패스워드 변경에 실패하였습니다,\n" +
                    "U006 - 비밀번호가 일치하지 않습니다,\n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<Void> passwordChange(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PasswordChangeDto passChangeDto);

    @Operation(summary = "이메일 수정 API", description = "공백 없이 이메일 형식을 지켜서 이메일을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 수정 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<Void> changeEmail(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid ChangeEmailDto changeEmailDto);

    @Operation(summary = "비밀번호 확인 API", description = "입력한 비밀번호가 일치한지 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 확인 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "U006 - 비밀번호가 일치하지 않습니다,\n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<Void> verifyPassword(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody PasswordVerifyDto password
    );

    @Operation(summary = "계정 탈퇴 API", description = "계정 삭제 요청을 통해 계정 상태를 삭제 상태로 변경하고, 이후 일주일 후 작성한 모든 콘텐츠가 삭제됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "계정 탈퇴 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "A001 - 인증에 실패하였습니다,\n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<Void> deleteAccount(@AuthenticationPrincipal UserDetailsImpl userDetails);

    @Operation(summary = "계정 탈퇴 싱크 맞추기 개발용 API 테스트", description = "계정 탈퇴 스케쥴러 실행하는 개발용 테스트 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스케쥴러 실행 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<Void> deleteAccountScheduler();

    @Operation(summary = "작가 정보 GET API", description = "작가의 정보를 GET 하는 API 입니다. 작가 정보(필명, 이미지, 작가의 말, 컨택 메일, 이력, 1:1수업 소개글)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "작가 정보", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "A001 - 인증에 실패하였습니다,\n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<UserInfo> getUserInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );
}

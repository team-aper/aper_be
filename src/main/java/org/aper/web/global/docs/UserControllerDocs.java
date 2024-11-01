package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.aper.web.domain.user.dto.UserRequestDto.*;
import org.aper.web.domain.user.dto.UserResponseDto;
import org.aper.web.domain.user.dto.UserResponseDto.SignupResponseDto;
import org.aper.web.global.dto.ErrorResponseDto;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "User", description = "회원 API")
public interface UserControllerDocs {

    @Operation(summary = "회원가입 API", description = "회원가입을 위한 API입니다. 이메일, 비밀번호, 닉네임 등을 입력하여 요청을 보냅니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = SignupResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "U001 - 이미 가입된 이메일입니다,\n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<SignupResponseDto> signup(@RequestBody @Valid SignupRequestDto requestDto);

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

    @Operation(summary = "필명 수정 API",
            description = "공백과 특수문자를 사용하지 않고 2~10글자 사이로 필명을 수정합니다.<br>" +
                    "<h4> batch로도 묶어서 진행할 수 있는 요청입니다.<h4>"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "필명 수정 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<Void> changePenName(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid ChangePenNameDto changePenNameDto);

    @Operation(summary = "이메일 수정 API", description = "공백 없이 이메일 형식을 지켜서 이메일을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 수정 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<Void> changeEmail(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid ChangeEmailDto changeEmailDto);

    @Operation(summary = "작가 소개글 수정 API", description = "사용자가 자신의 소개글을 수정합니다. " +
                    "<h4> batch로도 묶어서 진행할 수 있는 요청입니다.<h4>"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "소개글 수정 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<Void> changeDescription(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ChangeDescriptionDto descriptionDto);

    @Operation(summary = "작가 필드 이미지 수정 API", description = "작가의 프로필 이미지를 수정합니다. form-data 형식으로 이미지 파일을 전송해야 합니다." +
            "<h4> batch로도 묶어서 진행할 수 있는 요청입니다. (batch로 묶어서 요청 할 경우 base64 인코딩 필요)<h4>"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "필드 이미지 수정 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<String> changeImage(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam("fieldImageFile") MultipartFile fieldImageFile);

    @Operation(summary = "작가 이력 생성, 수정 API",
            description = "작가의 이력(학력, 수상, 출간)을 생성 및 수정 합니다.<br>" +
                    "학력, 수상, 출간 각각 requestBody 담길 historyType으로 분류하여 처리하고 각각 EDUCATION, AWARD, PUBLICATION 으로 구분됩니다.<br>" +
                    "requestBody의 historyId를 null로 보내주면 생성, null이 아니라 값이 존재한다면 수정으로 처리됩니다.<br>" +
                    "수상, 출간 기록의 경우 requestBody의 endDate를 null로, 학력의 경우 값을 넣어 보내주면 됩니다.<br>" +
                    "이력의 종류와 상관없이 리스트 형태로 요청과 응답이 이루어집니다.<br>" +
                    "<h4> 종류와 상관없이 생성, 수정 모두 date, description은 필수 입력값입니다.<h4>" +
                    "<h4> batch로도 묶어서 진행할 수 있는 요청입니다.<h4>"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "작가 이력 작성 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<Void> changeHistory(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody List<HistoryRequestDto> historyDtoList
    );

    @Operation(summary = "컨택 메일 수정 API", description = "공백 없이 이메일 형식을 지켜서 컨택 이메일을 수정합니다." +
            "<h4> batch로도 묶어서 진행할 수 있는 요청입니다.<h4>"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "작가 이력 작성 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<Void> changeContactEmail(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid ChangeEmailDto changeEmailDto
    );

    @Operation(summary = "1:1 수업 소개 수정 API", description = "2000자까지 입력 가능한 수업 소개 내용입니다." +
            "<h4> batch로도 묶어서 진행할 수 있는 요청입니다.<h4>"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "작가 이력 작성 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<Void> changeClassDescription(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid ClassDescriptionRequestDto requestDto
    );

    @Operation(summary = "계정 탈퇴 API", description = "계정 삭제 요청을 통해 계정 상태를 삭제 상태로 변경하고, 이후 일주일 후 작성한 모든 콘텐츠가 삭제됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "계정 탈퇴 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "U006 - 비밀번호가 일치하지 않습니다,\n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<Void> deleteAccount(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody DeletePasswordDto deletePasswordDto);

    @Operation(summary = "계정 탈퇴 싱크 맞추기 개발용 API 테스트", description = "계정 탈퇴 스케쥴러 실행하는 개발용 테스트 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스케쥴러 실행 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<Void> deleteAccountScheduler();

    @Operation(summary = "리뷰작성 API", description = "리뷰작성을 위한 API입니다. 작성자id, 리뷰타입, 채팅방id 등을 입력하여 요청을 보냅니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "리뷰작성에 성공하였습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "U002 - 등록되지 않은 회원입니다.,\n" +
                    "CH002 - 존재하지 않는 채팅방입니다.)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<UserResponseDto.CreatedReviewDto> createReview(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid CreateReviewRequestDto requestDto);
}

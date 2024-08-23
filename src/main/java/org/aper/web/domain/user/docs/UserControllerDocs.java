package org.aper.web.domain.user.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.aper.web.domain.user.dto.UserRequestDto;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "회원 API")
public interface UserControllerDocs {
    @Operation(summary = "필명 수정 API", description = "공백과 특수문자를 사용하지 않고 2~10글자사이로 입력해주세요.")
    ResponseDto<Void> changePenName(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid UserRequestDto.ChangePenNameDto changePenNameDto
    );

    @Operation(summary = "이메일 수정 API", description = "공백으로 넘기지 않고 이메일 양식을 지켜서 입력해주세요.")
    ResponseDto<Void> changeEmail(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid UserRequestDto.ChangeEmailDto changeEmailDto
    );

    @Operation(summary = "작가 소개글 수정 API", description = "공백으로 넘기지 말아주세요.")
    ResponseDto<Void> changeDescription(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserRequestDto.ChangeDescriptionDto descriptionDto
    );

    @Operation(summary = "작가 필드 이미지 수정 API", description = "form-data 형식으로 넘겨주세요.")
    ResponseDto<String> changeImage(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("fieldImageFile") MultipartFile fieldImageFile
    );

    @Operation(summary = "계정 탈퇴 API", description = "계정 삭제 요청에 성공할 경우 바로 계정 삭제가 되는게 아니라 삭제 상태, 로그인 불가능 상태로 변경됩니다. 이후 일주일 경과 후 해당 계정이 작성한 스토리, 에피소드가 일괄 삭제됩니다.")
    ResponseDto<Void> deleteAccount(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserRequestDto.DeletePasswordDto deletePasswordDto
    );
}

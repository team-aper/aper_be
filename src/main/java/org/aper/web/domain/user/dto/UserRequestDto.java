package org.aper.web.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.aper.web.domain.user.valid.UserValidationGroup.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;

public class UserRequestDto {

    public record SignupRequestDto(
            @NotBlank(message = "필명을 입력해주세요.", groups = NicknameBlankGroup.class)
            @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "필명은 특수문자를 제외한 2~10자리를 입력 해주세요.", groups = NicknamePatternGroup.class)
            String penName,
            @Schema(description = "이메일", example = "test@email.com")
            @NotBlank(message = "이메일을 입력해주세요.", groups = EmailBlankGroup.class)
            @Email(message = "이메일 형식이 아닙니다.", groups = EmailGroup.class)
            String email,
            @Schema(description = "비밀번호", example = "Test123!")
            @NotBlank(message = "비밀번호를 입력해주세요.", groups = PasswordBlankGroup.class)
            @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_~]).{8,15}$",
                    message = "비밀번호는 영어 소문자, 숫자, 특수문자를 포함한 8~15자리 입니다.", groups = PasswordPatternGroup.class)
            String password
            ) {
    }

    public record LoginRequestDto(
            @NotBlank(message = "이메일을 입력해 주세요.")
            String email,
            @NotBlank(message = "비밀번호를 입력해 주세요.")
            String password
    ){}

    public record EmailSendDto(
            @NotBlank(message = "이메일을 입력해주세요.", groups = NotBlankGroup.class)
            @Email(message = "잘못된 이메일 형식입니다.", groups = EmailGroup.class)
            String email
    ){}

    public record EmailAuthDto(
            @NotBlank(message = "이메일을 입력해주세요.", groups = NotBlankGroup.class)
            @Email(message = "잘못된 이메일 형식입니다.", groups = EmailGroup.class)
            String email,
            @NotBlank(message = "인증 코드를 입력해주세요", groups = NotBlankGroup.class)
            String authCode
    ){}

    public record PasswordChangeDto(
            @NotBlank(message = "현재 비밀번호를 입력해주세요.")
            String originPassword,
            @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
            String newPassword
    ){}

    public record ChangePenNameDto(
            @NotBlank(message = "필명을 입력해주세요.", groups = NicknameBlankGroup.class)
            @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "필명은 특수문자를 제외한 2~10자리를 입력 해주세요.", groups = NicknamePatternGroup.class)
            String penName
    ){}

    public record ChangeBatchImageDto(
            @NotBlank(message = "base64로 변환된 이미지를 입력해주세요.", groups = NotBlankGroup.class)
            String imageBase64
    ){}

    public record ChangeEmailDto(
            @NotBlank(message = "이메일을 입력해주세요.", groups = EmailBlankGroup.class)
            @Email(message = "이메일 형식이 아닙니다.", groups = EmailGroup.class)
            String email
    ){}

    public record ChangeDescriptionDto(
            @NotBlank(message = "작가 소개를 입력해주세요.", groups = NotBlankGroup.class)
            String description
    ){}

    public record DeletePasswordDto(
            @NotBlank(message = "비밀번호를 입력해 주세요.", groups = NotBlankGroup.class)
            String password
    ){}

    public record HistoryRequestDto(
            Long historyId,
            @NotBlank(message = "이력의 종류를 입력해 주세요.", groups = NotBlankGroup.class)
            String historyType,
            String endDateType,
            String startDateType,

            @NotBlank(message = "날짜를 입력해주세요.", groups = NotBlankGroup.class)
            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth date,

            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth endDate,
            String description
    ){}

    public record ClassDescriptionRequestDto(
            @NotBlank(message = "수업 소개를 입력해주세요.", groups = NotBlankGroup.class)
            @Size(max = 2000, message = "수업 소개는 최대 2000자까지 입력할 수 있습니다.")
            String description
    ) {}
}

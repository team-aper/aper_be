package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.aper.web.domain.review.dto.ReviewRequestDto.*;
import org.aper.web.domain.review.dto.ReviewResponseDto.*;
import org.aper.web.global.dto.ErrorResponseDto;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Review", description = "리뷰 API")
public interface ReviewControllerDocs {
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
    ResponseDto<CreatedReviewDto> createReview(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid CreateReviewRequestDto requestDto);
}

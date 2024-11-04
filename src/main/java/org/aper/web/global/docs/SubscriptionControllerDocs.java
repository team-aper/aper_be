package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aper.web.domain.subscription.dto.SubscriptionResponseDto.AuthorRecommendations;
import org.aper.web.domain.subscription.dto.SubscriptionResponseDto.IsSubscribed;
import org.aper.web.domain.subscription.dto.SubscriptionResponseDto.SubscribedAuthors;
import org.aper.web.domain.subscription.dto.SubscriptionResponseDto.IsSubscriber;
import org.aper.web.global.dto.ErrorResponseDto;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Subscription", description = "작가 구독 관련 API 목록")
public interface SubscriptionControllerDocs {

    @Operation(summary = "작가 구독", description = "지정된 작가를 구독합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구독 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "작가 또는 사용자 정보 찾을 수 없음 (ErrorCode: AUTHOR_NOT_FOUND, SUBSCRIBER_NOT_FOUND)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<Void> subscribe(
            @Parameter(description = "작가 ID", required = true) @PathVariable Long authorId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(summary = "구독 여부 확인", description = "지정된 작가에 대한 구독 여부를 확인합니다."+"<br>해당 사항을 확인 하여 작가의 페이지에서 작가를 구독하거나 구독을 해지할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구독 여부 확인 성공", content = @Content(schema = @Schema(implementation = IsSubscribed.class))),
            @ApiResponse(responseCode = "404", description = "작가 또는 사용자 정보 찾을 수 없음 (ErrorCode: AUTHOR_NOT_FOUND, SUBSCRIBER_NOT_FOUND)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<IsSubscribed> isSubscribed(
            @Parameter(description = "작가 ID", required = true) @PathVariable Long authorId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(summary = "구독자 여부 확인", description = "현재 사용자가 하나 이상의 구독이 있는지 여부를 확인합니다." +
            "<br>해당 사항을 확인 하여 true일 시 사용자가 구독한 작가 목록을 반환하고," +
            "<br>false일 시 장르별 추천 작가 목록을 반환할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구독자 여부 확인 성공", content = @Content(schema = @Schema(implementation = IsSubscriber.class))),
            @ApiResponse(responseCode = "404", description = "사용자 정보 찾을 수 없음 (ErrorCode: SUBSCRIBER_NOT_FOUND)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<IsSubscriber> isSubscriber(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(summary = "구독한 작가 목록 조회", description = "사용자가 구독한 작가 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구독한 작가 목록 조회 성공", content = @Content(schema = @Schema(implementation = SubscribedAuthors.class))),
            @ApiResponse(responseCode = "404", description = "사용자 정보 찾을 수 없음 (ErrorCode: SUBSCRIBER_NOT_FOUND)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<SubscribedAuthors> getSubscribedAuthors(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "추천 작가 목록 조회", description = "장르별로 추천 작가 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추천 작가 목록 조회 성공", content = @Content(schema = @Schema(implementation = AuthorRecommendations.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<AuthorRecommendations> getRecommendedAuthors(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size
    );

    @Operation(summary = "작가 구독 해제", description = "지정된 작가에 대한 구독을 해제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구독 해제 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "작가 또는 구독 정보를 찾을 수 없음 (ErrorCode: AUTHOR_NOT_FOUND, SUBSCRIPTION_NOT_FOUND)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<Void> unsubscribe(
            @Parameter(description = "작가 ID", required = true) @PathVariable Long authorId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );
}

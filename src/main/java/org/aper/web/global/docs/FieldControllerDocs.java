package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aper.web.domain.field.dto.FieldResponseDto.DetailsResponseDto;
import org.aper.web.domain.field.dto.FieldResponseDto.FieldHeaderResponseDto;
import org.aper.web.domain.field.dto.FieldResponseDto.HomeResponseDto;
import org.aper.web.domain.field.dto.FieldResponseDto.StoriesResponseDto;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Field", description = "작가의 필드에서 필요한 API")
public interface FieldControllerDocs {

    @Operation(summary = "필드 헤더 작가 정보 get API", description = "필드 페이지 상단에 필명, 작가 소개, 필드 이미지를 보여줌")
    ResponseDto<FieldHeaderResponseDto> getAuthorInfo(
            @PathVariable Long authorId
    );

    @Operation(summary = "필드 홈 get API", description = "토큰 필수 x, 본인의 필드일 경우 모든 에피소드를 보여줌")
    ResponseDto<HomeResponseDto> getFieldHomeData(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long authorId
    );

    @Operation(summary = "이야기 별 목록 get API", description = "토큰 필수 x, 본인의 필드일 경우 모든 스토리를 보여줌")
    ResponseDto<StoriesResponseDto> getStoriesData(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long authorId
    );

    @Operation(summary = "작가 정보 get API", description = "토큰 필수 x")
    ResponseDto<DetailsResponseDto> getDetailsData(
            @PathVariable Long authorId
    );
}

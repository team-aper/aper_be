package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aper.web.domain.episode.dto.EpisodeResponseDto;
import org.aper.web.domain.story.dto.StoryRequestDto.CoverChangeDto;
import org.aper.web.domain.story.dto.StoryRequestDto.StoryCreateDto;
import org.aper.web.domain.story.dto.StoryResponseDto.CreatedStoryDto;
import org.aper.web.domain.story.dto.StoryResponseDto.GetStoryDto;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "스토리 관련 API", description = "스토리 CRUD 관련 API 목록")
public interface StoryControllerDocs {

    @Operation(summary = "스토리 생성", description = "새로운 스토리를 생성합니다.")
    @PostMapping
    ResponseDto<CreatedStoryDto> createStory(
            @Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "스토리 생성 요청 데이터", required = true) @RequestBody StoryCreateDto storyCreateDto
    );

    @Operation(summary = "스토리 조회", description = "특정 ID에 해당하는 스토리의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스토리 조회 성공", content = @Content(schema = @Schema(implementation = GetStoryDto.class)))
    })
    @GetMapping("/{storyId}")
    ResponseDto<GetStoryDto> getStory(
            @Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "조회할 스토리의 ID", required = true) @PathVariable Long storyId
    );

    @Operation(summary = "스토리 커버 변경", description = "특정 ID에 해당하는 스토리의 커버 이미지를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스토리 커버 변경 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PutMapping("/{storyId}/cover")
    ResponseDto<Void> changeCover(
            @Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "커버를 변경할 스토리의 ID", required = true) @PathVariable Long storyId,
            @Parameter(description = "새 커버 정보", required = true) @RequestBody CoverChangeDto coverChangeDto
    );

    @Operation(summary = "스토리 공개 상태 변경", description = "특정 ID에 해당하는 스토리의 공개 상태를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스토리 공개 상태 변경 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PutMapping("/{storyId}/publish")
    ResponseDto<Void> changeStoryPublicStatus(
            @Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "공개 상태를 변경할 스토리의 ID", required = true) @PathVariable Long storyId
    );

    @Operation(summary = "스토리 삭제", description = "특정 ID에 해당하는 스토리를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스토리 삭제 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @DeleteMapping("/{storyId}")
    ResponseDto<Void> deleteStory(
            @Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "삭제할 스토리의 ID", required = true) @PathVariable Long storyId
    );

    @Operation(summary = "에피소드 생성", description = "특정 스토리 ID에 해당하는 새로운 에피소드 템플릿을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "에피소드 생성 성공", content = @Content(schema = @Schema(implementation = EpisodeResponseDto.CreatedEpisodeDto.class)))
    })
    @PostMapping("/{storyId}/episode/{chapter}")
    ResponseDto<EpisodeResponseDto.CreatedEpisodeDto> createEpisode(
            @Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "에피소드를 생성할 스토리의 ID", required = true) @PathVariable Long storyId,
            @Parameter(description = "생성할 에피소드의 chapter", required = true) @PathVariable Long chapter
    );


}

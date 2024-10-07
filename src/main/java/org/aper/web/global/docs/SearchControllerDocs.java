package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aper.web.domain.search.entity.dto.SearchDto.SearchAuthorResponseDto;
import org.aper.web.domain.search.entity.dto.SearchDto.SearchPenNameResponseDto;
import org.aper.web.domain.search.entity.dto.SearchDto.SearchStoryResponseDto;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.global.dto.ErrorResponseDto;
import org.aper.web.global.dto.ResponseDto;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "검색 API")
public interface SearchControllerDocs {
    @Operation(summary = "스토리 제목, 에피소드 제목, 에피소드 내용을 기반으로 검색할때의 API",
            description = "page와 size는 입력하지 않을 시 각각 0, 10이 기본값입니다.<br>" +
                    "장르를 입력하지 않을 경우 전체 장르에서 검색을 시도합니다.<br>" +
                    "필터에 입력되는 문자를 기반으로 스토리 제목, 에피소드 제목, 에피소드 내용 이 세개 중 하나라도 포함된 경우를 탐색하며<br>" +
                    "스토리 제목, 에피소드 제목에 포함된다면 응답에 들어가는 에피소드 내용은 첫 에피소드의 80글자를 전달하고,<br>" +
                    "에피소드의 내용에 포함되었다면 해당 내용에서 검색했던 단어를 기준으로 앞 40글자 뒤 40글자를 전달합니다.<br>" +
                    "<h4> filter값은 필수 입력값 입니다. <h4>"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = SearchStoryResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
            ResponseDto<SearchStoryResponseDto> getSearchStory(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) StoryGenreEnum genre,
            @RequestParam String filter
    );

    @Operation(summary = "작가 필명만 검색할때 API",
            description = "page와 size는 입력하지 않을 시 각각 0, 10이 기본값입니다.<br>" +
                    "검색 문자를 포함한 모든 작가를 탐색합니다.<br>" +
                    "<h4> 입력시 자동완성 기능같은 경우의 API입니다. 필명만 보냅니다 <h4>"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "필명 검색 성공", content = @Content(schema = @Schema(implementation = SearchPenNameResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<SearchPenNameResponseDto> getSearchAuthorOnlyPenName(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam String penName
    );

    @Operation(summary = "작가 필명을 기반으로 검색할때의 API",
            description = "page와 size는 입력하지 않을 시 각각 0, 10이 기본값입니다.<br>" +
                    "검색 문자를 포함한 모든 작가를 탐색합니다." +
                    "<h4>userId, penName 등등 user와 관련된 추가정보까지 한번에 보냅니다.<h4>"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "작가 검색 성공", content = @Content(schema = @Schema(implementation = SearchAuthorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode 목록: \n" +
                    "C002 - 유효성 검사 실패)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<SearchAuthorResponseDto> getSearchAuthor(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam String penName
    );

    @Operation(summary = "mysql의 Episode테이블과 엘라스틱서치의 싱크를 맞추는 API",
            description = "실제 서비스에서는 사용되지 않는 개발용 API 입니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "싱크 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<Void> testSyncEpisode();

    @Operation(summary = "mysql의 User테이블과 엘라스틱서치의 싱크를 맞추는 API",
            description = "실제 서비스에서는 사용되지 않는 개발용 API 입니다."
    )
    ResponseDto<Void> testSyncUser();
}
package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aper.web.domain.search.entity.dto.SearchDto;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
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
            ResponseDto<SearchDto.SearchStoryResponseDto> getSearchStory(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) StoryGenreEnum genre,
            @RequestParam String filter
    );

    @Operation(summary = "작가 필명을 기반으로 검색할때의 API",
            description = "page와 size는 입력하지 않을 시 각각 0, 10이 기본값입니다.<br>" +
                    "검색 문자를 포함한 모든 작가를 탐색합니다." +
                    "<h4> penName값은 필수 입력값 입니다. <h4>"
    )
    ResponseDto<SearchDto.SearchAuthorResponseDto> getSearchAuthor(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam String penName
    );
}
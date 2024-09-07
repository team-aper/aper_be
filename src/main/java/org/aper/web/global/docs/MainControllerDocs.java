package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aper.web.domain.main.dto.response.GetCurationsResponseDto;
import org.aper.web.domain.main.dto.response.GetEpisodesResponseDto;
import org.aper.web.domain.main.dto.response.GetUsersResponseDto;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.global.dto.ResponseDto;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Main", description = "메인 페이지 API")
public interface MainControllerDocs {

    @Operation(summary = "추천하는 이야기 API", description = "메인 페이지에 표시되는 이야기 큐레이션 목록을 가져옵니다.")
    ResponseDto<List<GetCurationsResponseDto>> getCurations(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "3", required = false) int size
    );

    @Operation(summary = "지금 주목받는 작가의 필드 API", description = "메인 페이지에 표시되는 작가 목록을 가져옵니다.")
    ResponseDto<List<GetUsersResponseDto>> getUsers(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "6", required = false) int size
    );

    @Operation(summary = "새로운 이야기 API", description = "메인 페이지에 표시되는 에피소드 목록을 가져옵니다. 장르를 선택할 수 있습니다.")
    ResponseDto<List<GetEpisodesResponseDto>> getEpisodes(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "5", required = false) int size,
            @RequestParam(required = false) StoryGenreEnum storyGenre
    );
}

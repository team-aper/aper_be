package org.aper.web.domain.curation.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.curation.dto.response.GetCurationsResponseDto;
import org.aper.web.domain.curation.service.CurationService;
import org.aper.web.global.dto.ResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "curation-controller", description = "Curation API")
@RestController
@RequestMapping("/curation")
@RequiredArgsConstructor
@Slf4j(topic = "큐레이션 컨트롤러")
public class CurationController {
    private final CurationService curationService;

    @GetMapping("/main")
    public ResponseDto<List<GetCurationsResponseDto>> getCurations(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "3", required = false) int size
    ) {
        final List<GetCurationsResponseDto> responseDtoList = curationService.getCurations(
                page, size
        );
        return ResponseDto.success("Get Curations in main page", responseDtoList);
    }

}

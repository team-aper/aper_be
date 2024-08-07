package org.aper.web.domain.field.controller;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.field.dto.HomeResponseDto;
import org.aper.web.domain.field.service.FieldService;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/field")
@RequiredArgsConstructor
public class FiledController {
    private final FieldService fieldService;
    @GetMapping("/home")
    public ResponseDto<HomeResponseDto> getFieldHomeData(
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        HomeResponseDto fieldHomeData = fieldService.getFieldHomeData(userDetails);
        return ResponseDto.success("작가 필드 홈 데이터", fieldHomeData);
    }
}

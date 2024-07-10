package org.example.springaper.domain.user.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.springaper.domain.user.dto.SignupRequestDto;
import org.example.springaper.domain.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
//    @PostMapping("/user/signup")
//    public ResponseDto<SignupResponseDto> signup(
//            @RequestBody @Valid SignupUserRequestDto requestDto) {
//        SignupResponseDto responseDto = userService.signup(requestDto);
//        return ResponseDto.success("회원가입 성공", responseDto);
//    }
}
package org.aper.web.domain.user.service;

import jakarta.validation.Valid;
import org.aper.web.domain.curation.dto.response.GetCurationsResponseDto;
import org.aper.web.domain.curation.entity.Curation;
import org.aper.web.domain.user.dto.UserRequestDto.*;
import org.aper.web.domain.user.dto.UserResponseDto.*;
import org.aper.web.domain.user.dto.response.GetUsersForMainResponseDto;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.entity.UserRoleEnum;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findUser(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
    }

    public SignupResponseDto signupUser(@Valid SignupRequestDto requestDto) {
        String penName = requestDto.penName();
        String email = requestDto.email();
        String password = passwordEncoder.encode(requestDto.password());

        if (userRepository.existsByEmail(email)) {
            throw new ServiceException(ErrorCode.ALREADY_EXIST_EMAIL);
        }

        User user = User.builder()
                .email(email)
                .password(password)
                .penName(penName)
                .role(UserRoleEnum.USER)
                .build();

        userRepository.save(user);

        return new SignupResponseDto(penName);
    }

    public List<GetUsersForMainResponseDto> getUsersForMain(int page, int size) {
        PageRequest pageRequest = PageRequest.of(Math.max(page - 1, 0), size, Sort.Direction.ASC, "userId");

        List<User> users  = userRepository.findAllForMain(pageRequest).getContent();
        List<GetUsersForMainResponseDto> responseDtoList = new ArrayList<>();

        for (User user : users) {
            responseDtoList.add(new GetUsersForMainResponseDto(user));
        }

        return responseDtoList;
    }
}
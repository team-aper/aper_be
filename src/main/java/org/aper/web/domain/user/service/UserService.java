package org.aper.web.domain.user.service;

import jakarta.validation.Valid;
import org.aper.web.domain.user.dto.UserRequestDto.*;
import org.aper.web.domain.user.dto.UserResponseDto.*;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.exception.ErrorCode;
import org.aper.web.global.exception.ServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public SignupResponseDto signup(@Valid SignupRequestDto requestDto) {
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
                .build();

        userRepository.save(user);

        return new SignupResponseDto(penName);
    }
}
package org.aper.web.domain.user.service;


import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.user.dto.UserRequestDto.*;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PasswordService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void changePassword(User user, PasswordChangeDto passChangeDto) {

        if(!passwordEncoder.matches(passChangeDto.originPassword(), user.getPassword()) && passChangeDto.newPassword().equals(passChangeDto.originPassword())) {
            throw new ServiceException(ErrorCode.PASSWORD_CHANGE_ERROR);
        }

        User updateUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        updateUser.updatePassword(passwordEncoder.encode(passChangeDto.newPassword()));
        log.info(passChangeDto.newPassword());
    }
}

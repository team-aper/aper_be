package org.aper.web.domain.chat.validator;

import org.aper.web.domain.user.entity.User;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public User validateUserExists(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ServiceException(ErrorCode.USER_NOT_FOUND));
    }
}

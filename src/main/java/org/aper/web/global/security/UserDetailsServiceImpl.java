package org.aper.web.global.security;

import com.aperlibrary.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmailWithOutDeleteAccount(email)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        return new UserDetailsImpl(user, Collections.emptyMap());
    }

    public UserDetails loadUserByUsername(String email, Map<String, Object> attributes) {
        User user = userRepository.findByEmailWithOutDeleteAccount(email)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        return new UserDetailsImpl(user, attributes);
    }
}
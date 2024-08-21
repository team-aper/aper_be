package org.aper.web.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.user.dto.UserRequestDto;
import org.aper.web.domain.user.entity.DeleteAccount;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.DeleteAccountRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteAccountService {
    private final DeleteAccountRepository deleteAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public void deleteAccount(User user, UserRequestDto.DeletePasswordDto deletePasswordDto) {
        if(!passwordEncoder.matches(deletePasswordDto.password(), user.getPassword())) {
            throw new ServiceException(ErrorCode.INCORRECT_PASSWORD);
        }
        DeleteAccount account = new DeleteAccount(user);
        deleteAccountRepository.save(account);
    }
}

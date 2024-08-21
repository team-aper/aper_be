package org.aper.web.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.user.dto.UserRequestDto.DeletePasswordDto;
import org.aper.web.domain.user.entity.DeleteAccount;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.DeleteAccountRepository;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteService {
    private final DeleteAccountRepository deleteAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public void deleteAccount(User user, DeletePasswordDto deletePasswordDto) {
        if(!passwordEncoder.matches(deletePasswordDto.password(), user.getPassword())) {
            throw new ServiceException(ErrorCode.INCORRECT_PASSWORD);
        }
        DeleteAccount account = new DeleteAccount(user);
        user.updateDeleteAccount(account);

        deleteAccountRepository.save(account);
        userRepository.save(user);
    }

    @Transactional
    public void deleteAccountScheduler() {
        LocalDateTime period = LocalDateTime.now().minusWeeks(1);
        List<DeleteAccount> deleteAccounts = deleteAccountRepository.findAllToDelete(period);

        if(deleteAccounts.isEmpty()) {
            log.info("there is nothing account to delete");
            return;
        }

        List<User> usersToDelete = deleteAccounts.stream()
                .map(DeleteAccount::getUser)
                .toList();

        deleteAccountRepository.deleteAll(deleteAccounts);

        userRepository.deleteAll(usersToDelete);
    }
}


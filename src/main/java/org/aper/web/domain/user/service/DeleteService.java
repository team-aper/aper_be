package org.aper.web.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
public class DeleteService {
    private final DeleteAccountRepository deleteAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void deleteAccount(User user, DeletePasswordDto deletePasswordDto) {
        if(!passwordEncoder.matches(deletePasswordDto.password(), user.getPassword())) {
            throw new ServiceException(ErrorCode.INCORRECT_PASSWORD);
        }
        DeleteAccount account = new DeleteAccount(user);
        deleteAccountRepository.save(account);
    }

    @Transactional
    public void deleteAccountScheduler() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        List<DeleteAccount> deleteAccounts = deleteAccountRepository.findAllToDelete(oneWeekAgo);

        List<User> usersToDelete = deleteAccounts.stream()
                .map(DeleteAccount::getUser)
                .toList();

        deleteAccountRepository.deleteAll(deleteAccounts);

        userRepository.deleteAll(usersToDelete);
    }
}

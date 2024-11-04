package org.aper.web.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.kafka.service.KafkaUserProducerService;
import org.aper.web.domain.user.entity.DeleteAccount;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.DeleteAccountRepository;
import org.aper.web.domain.user.repository.UserRepository;
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
    private final KafkaUserProducerService producerService;

    @Transactional
    public void deleteAccount(User user) {
        DeleteAccount account = new DeleteAccount(user);
        user.updateDeleteAccount(account);

        deleteAccountRepository.save(account);
        userRepository.save(user);
        producerService.sendDelete(user.getUserId());
    }

    @Transactional
    public void deleteAccountScheduler() {
        LocalDateTime period = LocalDateTime.now().minusWeeks(1);
        List<User> deleteAccounts = deleteAccountRepository.findAllToDelete(period).stream()
                .map(DeleteAccount::getUser)
                .toList();

        if(deleteAccounts.isEmpty()) {
            return;
        }

        userRepository.deleteAll(deleteAccounts);
    }
}



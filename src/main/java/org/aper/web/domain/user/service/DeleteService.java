package org.aper.web.domain.user.service;

import org.aper.web.domain.user.entity.DeleteAccount;
import org.aper.web.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.kafka.service.KafkaUserProducerService;
import org.aper.web.domain.user.repository.DeleteAccountRepository;
import org.aper.web.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class DeleteService {
    private final DeleteAccountRepository deleteAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @Autowired(required = false)
    private KafkaUserProducerService producerService;

    public DeleteService(DeleteAccountRepository deleteAccountRepository,
                         PasswordEncoder passwordEncoder,
                         UserRepository userRepository) {
        this.deleteAccountRepository = deleteAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional
    public void deleteAccount(User user) {
        DeleteAccount account = new DeleteAccount(user);
        user.updateDeleteAccount(account);

        deleteAccountRepository.save(account);
        userRepository.save(user);
        if (producerService != null) {
            producerService.sendDelete(user.getUserId());
        }
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



package org.aper.web.global.dummies;

import com.aperlibrary.user.entity.User;
import com.aperlibrary.user.entity.UserHistory;
import com.aperlibrary.user.entity.constant.EndDateTypeEnum;
import com.aperlibrary.user.entity.constant.HistoryTypeEnum;
import com.aperlibrary.user.entity.constant.StartDateTypeEnum;
import com.aperlibrary.user.entity.constant.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.aper.web.domain.user.repository.UserHistoryRepository;
import org.aper.web.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDataSeederService {

    private final UserRepository userRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final PasswordEncoder passwordEncoder;
    Faker faker = new Faker();


    @Transactional
    public List<User> generateDummyUsers(int userCount) {
        List<User> users = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < userCount; i++) {
            String koreanName = KoreanNameGenerator.generateKoreanName();
            String email = faker.internet().emailAddress();
            User user = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode("Test123!"))
                    .role(UserRoleEnum.USER)
                    .penName(koreanName)
                    .build();

            if (i <= 11) {
                user.updateFieldImage("/images/im" + (i + 1) + ".jpg");
            }
            if (i > 11){
                user.updateFieldImage("/images/im" + 100 + ".jpg");
            }

            if (random.nextBoolean()) { // true or false 랜덤 결정
                user.updateContactEmail(email);
            }

            if (i < 6){
                user.isExposed();
            }

            users.add(user);
        }
        return userRepository.saveAll(users);
    }

    @Transactional
    public void generateUserHistories(List<User> users) {
        users.forEach(user -> {
            List<UserHistory> userHistories = new ArrayList<>();
            userHistories.add(createEducationHistory(user));
            userHistories.add(createAwardHistory(user));
            userHistories.add(createPublicationHistory(user));
            userHistoryRepository.saveAll(userHistories);
        });
    }

    private UserHistory createEducationHistory(User user) {
        UserHistory history = new UserHistory();
        history.updateEducation(
                YearMonth.now().minusYears(2),
                YearMonth.now(),
                "Bachelor of Science in Computer Science",
                EndDateTypeEnum.GRADUATED,
                StartDateTypeEnum.ENTERED
        );
        history.updateUser(user);
        return history;
    }

    private UserHistory createAwardHistory(User user) {
        UserHistory history = new UserHistory();
        history.updateAwardPublication(
                YearMonth.now().minusMonths(6),
                "Best Developer Award",
                HistoryTypeEnum.AWARD
        );
        history.updateUser(user);
        return history;
    }

    private UserHistory createPublicationHistory(User user) {
        UserHistory history = new UserHistory();
        history.updateAwardPublication(
                YearMonth.now().minusYears(1),
                "Published Research Paper",
                HistoryTypeEnum.PUBLICATION
        );
        history.updateUser(user);
        return history;
    }
}

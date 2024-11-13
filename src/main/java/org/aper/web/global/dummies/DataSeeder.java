package org.aper.web.global.dummies;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserDataSeederService userDataSeederService;
    private final StoryDataSeederService storyDataSeederService;
    private final UserRepository userRepository;
    private final Faker faker = new Faker();

    private static final String[] GENRES = {"ART", "CRITICISM", "DAILY", "HORROR", "POETRY", "QUEER", "ROMANCE", "SF", "SOCIETY"};
    private static final String[] ROUTINES = {"FREE", "SHORT", "LONG", "POETRY"};
    private static final String[] LINE_STYLES = {"MUNSEOBU_BATANGCHE", "KO_PUB_BATANGCHE", "NOTO_SERIF_KR", "NAVER_NANOOM_MYEONGJO"};

    @Override
    public void run(String... args) {

        if (userRepository.count() > 0) {
            log.info("Data already exists. Skipping DataSeeder.");
            return;
        }

        log.info("DataSeeder is running...");

        // 1. Generate dummy Users
        List<User> users = userDataSeederService.generateDummyUsers(10);

        // 2. Generate User Histories
        userDataSeederService.generateUserHistories(users);

        // 3. Generate Stories, Episodes, and Paragraphs
        storyDataSeederService.generateStoriesWithEpisodes(users, GENRES, ROUTINES, LINE_STYLES, faker);

        // 4. Generate Subscriptions
        storyDataSeederService.generateSubscriptions(users, faker);

        // 5. Generate Curation
        storyDataSeederService.generateCurations();

        log.info("DataSeeder completed.");
    }
}

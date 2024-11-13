package org.aper.web.global.dummies;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.aper.web.domain.curation.entity.Curation;
import org.aper.web.domain.curation.repository.CurationRepository;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.paragraph.entity.Paragraph;
import org.aper.web.domain.paragraph.entity.TextAlignEnum;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.domain.story.entity.constant.StoryLineStyleEnum;
import org.aper.web.domain.story.entity.constant.StoryRoutineEnum;
import org.aper.web.domain.story.repository.StoryRepository;
import org.aper.web.domain.subscription.entity.Subscription;
import org.aper.web.domain.subscription.repository.SubscriptionRepository;
import org.aper.web.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoryDataSeederService {

    private final StoryRepository storyRepository;
    private final EpisodeRepository episodeRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final CurationRepository curationRepository;

    @Transactional
    public void generateStoriesWithEpisodes(List<User> users, String[] genres, String[] routines, String[] lineStyles, Faker faker) {
        for (String genre : genres) {
            for (int i = 0; i < 8; i++) {
                StoryRoutineEnum routineEnum = StoryRoutineEnum.fromString(routines[i % routines.length]);
                User user = users.get(faker.random().nextInt(users.size()));

                Story story = Story.builder()
                        .title(faker.book().title())
                        .genre(StoryGenreEnum.fromString(genre))
                        .routine(routineEnum)
                        .lineStyle(StoryLineStyleEnum.fromString(lineStyles[i % lineStyles.length]))
                        .user(user)
                        .build();

                storyRepository.save(story);
                createEpisodesForStory(story, routineEnum, faker);
                log.info("Creating story: {} for genre: {}", i, genre);
                log.info("Generating episodes for story ID: {}", story.getId());
            }
        }
    }

    private void createEpisodesForStory(Story story, StoryRoutineEnum routineEnum, Faker faker) {
        log.info("Start generating episodes for Story ID: {}, Routine: {}", story.getId(), routineEnum.name());

        // `StoryRoutineEnum`을 사용해 에피소드 생성
        List<Episode> episodes = routineEnum.createEpisodes(story);
        log.info("createEpisodes returned {} episodes for Story ID: {}", episodes.size(), story.getId());

        if (episodes.isEmpty()) {
            log.warn("No episodes were generated for Story ID: {} with Routine: {}", story.getId(), routineEnum.name());
            return;
        }

        // **타이틀 업데이트 및 문단 추가**
        for (int i = 0; i < episodes.size(); i++) {
            Episode episode = episodes.get(i);

            // 타이틀 업데이트
            String title = faker.book().title() + " - Episode " + (i + 1);
            episode.updateTitle(title);

            // 문단 추가
            addParagraphsToEpisode(episode, faker);

            // onDisplay 업데이트
            episode.updateOnDisplay();

            log.info("Updated Episode {} with Title: {}, Paragraphs: {}", i + 1, title, episode.getParagraphs().size());
        }

        log.info("Generated {} episodes for Story ID: {}", episodes.size(), story.getId());

        // 저장
        episodeRepository.saveAll(episodes);

        // 저장 후 확인
        List<Episode> savedEpisodes = episodeRepository.findByStoryOrderByChapterAsc(story);
        log.info("Saved {} episodes in DB for Story ID: {}", savedEpisodes.size(), story.getId());

        // Story 업데이트
        story.addEpisodes(episodes);
        story.updateOnDisplay();
        storyRepository.save(story);

        log.info("Finished generating episodes for Story ID: {}", story.getId());
    }

    private void addParagraphsToEpisode(Episode episode, Faker faker) {
        String previousUuid = null;

        for (int i = 0; i < 5; i++) {
            String uuid = UUID.randomUUID().toString();
            String content = faker.lorem().paragraph();

            // 문단 생성
            Paragraph paragraph = Paragraph.builder()
                    .uuid(uuid)
                    .content(content)
                    .textAlign(TextAlignEnum.fromString("LEFT")) // 기본 정렬
                    .previousUuid(previousUuid)
                    .episode(episode)
                    .build();

            if (i == 0) {
                episode.updateDescription(content); // 첫 번째 문단을 설명(description)으로 설정
            }

            if (previousUuid != null) {
                episode.getParagraphs()
                        .get(episode.getParagraphs().size() - 1)
                        .updateNextUuid(uuid); // 이전 문단의 nextUuid 업데이트
            }

            previousUuid = uuid;
            episode.getParagraphs().add(paragraph); // 문단 추가
        }
    }


    @Transactional
    public void generateSubscriptions(List<User> users, Faker faker) {
        for (int i = 0; i < 10; i++) {
            Subscription subscription = Subscription.builder()
                    .author(users.get(faker.number().numberBetween(0, users.size())))
                    .subscriber(users.get(faker.number().numberBetween(0, users.size())))
                    .build();
            subscriptionRepository.save(subscription);
        }
    }

    @Transactional
    public void generateCurations() {
        List<Episode> eligibleEpisodes = episodeRepository.findByOnDisplayTrueAndStoryOnDisplayTrue();
        Collections.shuffle(eligibleEpisodes);

        List<Episode> selectedEpisodesForCuration = eligibleEpisodes.subList(0, Math.min(3, eligibleEpisodes.size()));
        selectedEpisodesForCuration.forEach(episode -> {
            Curation curation = Curation.builder().episode(episode).build();
            curationRepository.save(curation);
        });
    }
}

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
            }
        }
    }

    private void createEpisodesForStory(Story story, StoryRoutineEnum routineEnum, Faker faker) {

        // `StoryRoutineEnum`을 사용해 에피소드 생성
        List<Episode> episodes = routineEnum.createEpisodes(story);

        if (episodes.isEmpty()) {
            log.warn("No episodes were generated for Story ID: {} with Routine: {}", story.getId(), routineEnum.name());
            return;
        }

        // 타이틀 업데이트 및 문단 추가
        for (int i = 0; i < episodes.size(); i++) {
            Episode episode = episodes.get(i);

            // 타이틀 업데이트
            String title = faker.book().title();
            episode.updateTitle(title);

            // 문단 추가
            addParagraphsToEpisode(episode);

            // onDisplay 업데이트
            episode.updateOnDisplay();
        }

        // 저장
        episodeRepository.saveAll(episodes);

        // Story 업데이트
        story.addEpisodes(episodes);
        story.updateOnDisplay();
        storyRepository.save(story);
    }

    private void addParagraphsToEpisode(Episode episode) {
        String previousUuid = null;

        // 시 장르인 경우, DUMMY_POEMS를 기반으로 16개 문단 추가
        if (episode.getStory().getGenre() == StoryGenreEnum.POETRY) {
            for (int i = 0; i < 16; i++) {
                String uuid = UUID.randomUUID().toString();
                String content = ParagraphDummyData.getRandomPoetry(); // 시 문단 가져오기
                previousUuid = getString(episode, previousUuid, i, uuid, content);
            }
        } else {
            // 시 장르가 아닌 경우, 랜덤하게 8개 문단 추가
            for (int i = 0; i < 8; i++) {
                String uuid = UUID.randomUUID().toString();
                String content;
                // 장르에 따라 다른 문단 소스를 선택
                if (episode.getStory().getGenre() == StoryGenreEnum.DAILY) {
                    content =  ParagraphDummyData.getSequentialParagraph1(i); // 일상 장르
                } else if (episode.getStory().getGenre() == StoryGenreEnum.SOCIETY) {
                    content = ParagraphDummyData.getSequentialParagraph2(i);
                } else {
                    content = ParagraphDummyData.getRandomParagraph(); // 기본 랜덤 문단
                }

                previousUuid = getString(episode, previousUuid, i, uuid, content);
            }
        }
    }

    private String getString(Episode episode, String previousUuid, int i, String uuid, String content) {
        Paragraph paragraph = Paragraph.builder()
                .uuid(uuid)
                .content(content)
                .textAlign(TextAlignEnum.LEFT) // 기본 정렬
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
        return previousUuid;
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

        // 장르별로 에피소드 필터링
        Episode societyEpisode = getRandomEpisodeForGenre(eligibleEpisodes, StoryGenreEnum.SOCIETY);
        Episode dailyEpisode = getRandomEpisodeForGenre(eligibleEpisodes, StoryGenreEnum.DAILY);
        Episode poetryEpisode = getRandomEpisodeForGenre(eligibleEpisodes, StoryGenreEnum.QUEER);

        // 큐레이션 생성
        if (societyEpisode != null) {
            createCuration(societyEpisode);
        }
        if (dailyEpisode != null) {
            createCuration(dailyEpisode);
        }
        if (poetryEpisode != null) {
            createCuration(poetryEpisode);
        }

    }

    // 특정 장르의 랜덤 에피소드 선택
    private Episode getRandomEpisodeForGenre(List<Episode> episodes, StoryGenreEnum genre) {
        List<Episode> filteredEpisodes = new java.util.ArrayList<>(episodes.stream()
                .filter(episode -> episode.getStory().getGenre() == genre)
                .toList());

        if (filteredEpisodes.isEmpty()) {
            log.warn("No eligible episodes found for genre: {}", genre.name());
            return null;
        }

        Collections.shuffle(filteredEpisodes); // 랜덤 정렬
        return filteredEpisodes.get(0); // 첫 번째 에피소드 반환
    }

    // 큐레이션 생성 메서드
    private void createCuration(Episode episode) {
        Curation curation = Curation.builder()
                .episode(episode)
                .build();
        curationRepository.save(curation);
    }

}

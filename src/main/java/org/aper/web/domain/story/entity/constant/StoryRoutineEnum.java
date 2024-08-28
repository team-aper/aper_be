package org.aper.web.domain.story.entity.constant;

import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public enum StoryRoutineEnum {
    FREE {
        @Override
        public int getEpisodeCount() {
            return 0; // 자유루틴 에피소드 개수 제한 없음
        }

        @Override
        public List<Episode> createEpisodes(Story story) {
            return new ArrayList<>(); // 자유루틴은 에피소드 생성 없음
        }

        @Override
        public int calculateEpisodeDDay(LocalDateTime createdDate, int chapterNumber) {
            return 0; // 자유루틴은 D-day 계산 없음
        }
    },
    SHORT {
        @Override
        public int getEpisodeCount() {
            return 5; // 단편 총 5회
        }

        @Override
        public List<Episode> createEpisodes(Story story) {
            return generateEpisodes(story, getEpisodeCount());
        }

        @Override
        public int calculateEpisodeDDay(LocalDateTime createdDate, int chapterNumber) {
            return calculateDDay(createdDate, chapterNumber, 30); // 챕터마다 30일씩 증가
        }
    },
    LONG {
        @Override
        public int getEpisodeCount() {
            return 10; // 장편은 총 10회
        }

        @Override
        public List<Episode> createEpisodes(Story story) {
            return generateEpisodes(story, getEpisodeCount());
        }

        @Override
        public int calculateEpisodeDDay(LocalDateTime createdDate, int chapterNumber) {
            return calculateDDay(createdDate, chapterNumber, 40); // 챕터마다 40일씩 증가
        }
    },
    POETRY {
        @Override
        public int getEpisodeCount() {
            return 50; // 시집 총 50회
        }

        @Override
        public List<Episode> createEpisodes(Story story) {
            return generateEpisodes(story, getEpisodeCount());
        }

        @Override
        public int calculateEpisodeDDay(LocalDateTime createdDate, int chapterNumber) {
            return calculateDDay(createdDate, chapterNumber, 7); // 챕터마다 7일씩 증가
        }
    };

    public abstract int getEpisodeCount();
    public abstract List<Episode> createEpisodes(Story story);
    public abstract int calculateEpisodeDDay(LocalDateTime createdDate, int chapterNumber);

    // 공통 D-day 계산 로직을 하나의 메서드로 간소화
    protected int calculateDDay(LocalDateTime createdDate, int chapterNumber, int episodeDuration) {
        if (createdDate == null) {
            return 0;
        }

        int totalDuration = episodeDuration * chapterNumber; // 챕터 수에 따라 증가

        // LocalDate로 변환하여 날짜만 비교하도록 수정
        LocalDate startDate = createdDate.toLocalDate();
        LocalDate endDate = startDate.plusDays(totalDuration);

        return (int) ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }

    protected List<Episode> generateEpisodes(Story story, int episodeCount) {
        List<Episode> episodes = new ArrayList<>();
        for (int i = 1; i <= episodeCount; i++) {
            Episode episode = Episode.builder()
                    .chapter((long) i)
                    .story(story)
                    .build();
            episodes.add(episode);
        }
        return episodes;
    }

    public static StoryRoutineEnum fromString(String value) {
        for (StoryRoutineEnum routine : StoryRoutineEnum.values()) {
            if (routine.name().equalsIgnoreCase(value)) {
                return routine;
            }
        }
        throw new ServiceException(ErrorCode.INVALID_ROUTINE);
    }
}

package org.aper.web.domain.story.entity.constant;

import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.service.DDayService;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public enum StoryRoutineEnum {
    FREE {
        @Override
        public int getEpisodeCount() {
            return 0;
        }

        @Override
        public List<Episode> createEpisodes(Story story) {
            return new ArrayList<>();
        }

        @Override
        public int calculateEpisodeDDay(LocalDateTime createdDate, int chapterNumber, DDayService dDayService) {
            return 0;  // FREE 루틴은 D-Day 계산이 필요 없음
        }
    },
    SHORT {
        @Override
        public int getEpisodeCount() {
            return 5;
        }

        @Override
        public List<Episode> createEpisodes(Story story) {
            return generateEpisodes(story, getEpisodeCount());
        }

        @Override
        public int calculateEpisodeDDay(LocalDateTime createdDate, int chapterNumber, DDayService dDayService) {
            return dDayService.getDDay(createdDate, chapterNumber, 30);  // 캐싱된 서비스 호출
        }
    },
    LONG {
        @Override
        public int getEpisodeCount() {
            return 10;
        }

        @Override
        public List<Episode> createEpisodes(Story story) {
            return generateEpisodes(story, getEpisodeCount());
        }

        @Override
        public int calculateEpisodeDDay(LocalDateTime createdDate, int chapterNumber, DDayService dDayService) {
            return dDayService.getDDay(createdDate, chapterNumber, 40);  // 캐싱된 서비스 호출
        }
    },
    POETRY {
        @Override
        public int getEpisodeCount() {
            return 50;
        }

        @Override
        public List<Episode> createEpisodes(Story story) {
            return generateEpisodes(story, getEpisodeCount());
        }

        @Override
        public int calculateEpisodeDDay(LocalDateTime createdDate, int chapterNumber, DDayService dDayService) {
            return dDayService.getDDay(createdDate, chapterNumber, 7);  // 캐싱된 서비스 호출
        }
    };

    public abstract int getEpisodeCount();
    public abstract List<Episode> createEpisodes(Story story);
    public abstract int calculateEpisodeDDay(LocalDateTime createdDate, int chapterNumber, DDayService dDayService);

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
}

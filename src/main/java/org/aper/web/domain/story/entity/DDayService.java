package org.aper.web.domain.story.entity;

import org.springframework.cache.annotation.Cacheable;

import java.time.Duration;
import java.time.LocalDateTime;

public class DDayService {
    @Cacheable(value = "dDayCache", key = "#createdDate.toString() + '-' + #chapterNumber + '-' + #interval")
    public int getDDay(LocalDateTime createdDate, int chapterNumber, int interval) {
        LocalDateTime targetDate = createdDate.plusDays((long) chapterNumber * interval);
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(now, targetDate);
        long daysBetween = duration.toDays();

        return (int) daysBetween;
    }
}

package com.aperlibrary.story;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class DDayService {

    /**
     * 챕터 단위로 D-Day를 계산하는 메서드.
     * @param createdDate  시작 날짜 (생성일)
     * @param chapterNumber 챕터 번호
     * @param episodeDuration 챕터마다 걸리는 기간 (일 단위)
     * @return D-Day 값
     */
    public static int calculateDDay(LocalDateTime createdDate, int chapterNumber, int episodeDuration) {
        if (createdDate == null) {
            return 0;
        }

        int totalDuration = episodeDuration * chapterNumber; // 챕터 수에 따라 증가

        // LocalDate로 변환하여 날짜만 비교하도록 수정
        LocalDate startDate = createdDate.toLocalDate();
        LocalDate endDate = startDate.plusDays(totalDuration);

        return (int) ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }

    @Cacheable(value = "dDayCache", key = "#createdDate.toLocalDate().toString() + '_' + #chapterNumber")
    public int getDDay(LocalDateTime createdDate, int chapterNumber, int episodeDuration) {
        return calculateDDay(createdDate, chapterNumber, episodeDuration);
    }
}

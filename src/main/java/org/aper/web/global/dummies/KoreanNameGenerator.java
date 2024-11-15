package org.aper.web.global.dummies;

import java.util.Random;

public class KoreanNameGenerator {
    private static final String[] LAST_NAMES = {"김", "이", "박", "최", "정", "강", "조", "윤", "장", "서", "유", "오", "진"};
    private static final String[] FIRST_NAMES = {"민수", "서연", "지민", "현우", "은서", "도윤", "하윤", "예준", "유진", "수아", "수연", "지연", "종연", "다솔", "선오"};

    public static String generateKoreanName() {
        Random random = new Random();
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        return lastName + firstName;
    }
}

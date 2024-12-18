package org.aper.web.global.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

public class PasswordUtil {

    private static final int PASSWORD_LENGTH = 12; // 패스워드 길이
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        if (!password.toString().matches(".*[0-9].*") || !password.toString().matches(".*[!@#$%^&*()\\-_=+].*")) {
            return generateRandomPassword();
        }

        return password.toString();
    }

    public static String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}

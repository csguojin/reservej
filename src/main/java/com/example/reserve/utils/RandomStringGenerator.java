package com.example.reserve.utils;

import java.security.SecureRandom;

public class RandomStringGenerator {
    private static final String CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String geneRandStr(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARSET.length());
            result.append(CHARSET.charAt(randomIndex));
        }
        return result.toString();
    }
}

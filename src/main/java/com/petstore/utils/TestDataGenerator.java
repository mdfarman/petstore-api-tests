package com.petstore.utils;

public class TestDataGenerator {
    public static int safeParseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }
}

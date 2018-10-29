package com.ww.helper;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagHelper {
    static public String randomUUID() {
        return UUID.randomUUID().toString();
    }

    static public String randomUniqueUUID(Map map) {
        String uuid = randomUUID();
        while (map.containsKey(uuid)) {
            uuid = randomUUID();
        }
        return uuid;
    }

    static public String randomTag() {
        return randomUUID().substring(0, 7);
    }

    static public String prepareTag(String tag) {
        return tag.trim().replace("#", "");
    }

    static public boolean shouldPrepareTag(String tag) {
        return tag.length() <= 10;
    }

    static public boolean isCorrectTag(String tag) {
        if (tag.length() != 7) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[\\p{Alnum}]+$");
        Matcher matcher = pattern.matcher(tag);
        return matcher.matches();
    }
}

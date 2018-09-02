package com.ww.helper;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagHelper {
    static public String randomTag() {
        return UUID.randomUUID().toString().substring(0, 7);
    }

    static public String prepareTag(String tag) {
        return tag.trim().replace("#", "");
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

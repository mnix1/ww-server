package com.ww.helper;

import java.security.SecureRandom;
import java.util.List;

public class RandomHelper {

    public static SecureRandom random = new SecureRandom();

    public static <T> T randomElement(List<T> list) {
        return randomElement(list, 0, 0);
    }

    public static <T> T randomElement(List<T> list, int offsetBegin, int offsetEnd) {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(randomElementIndex(list, offsetBegin, offsetEnd));
    }


    public static int randomElementIndex(List list) {
        return randomElementIndex(list, 0, 0);
    }

    public static int randomElementIndex(List list, int offsetBegin, int offsetEnd) {
        return offsetBegin + random.nextInt(list.size() - offsetBegin - offsetEnd);
    }

}

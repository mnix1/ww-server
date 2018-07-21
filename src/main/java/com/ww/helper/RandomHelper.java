package com.ww.helper;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RandomHelper {

    public static SecureRandom random = new SecureRandom();

    public static int randomInteger(int from, int to) {
        return random.nextInt(to - from + 1) + from;
    }

    public static <T> List<T> randomElements(List<T> list, int count) {
        if (count > list.size()) {
            count = list.size();
        }
        Set<Integer> set = new HashSet<>(count);
        while (set.size() < count) {
            while (!set.add(random.nextInt(list.size()))) {
            }
        }
        return set.stream().map(index -> list.get(index)).collect(Collectors.toList());
    }

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

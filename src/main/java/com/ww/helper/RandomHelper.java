package com.ww.helper;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

public class RandomHelper {

    public static SecureRandom random = new SecureRandom();

    public static int randomInteger(int from, int to) {
        if (to - from <= 0) {
            return 0;
        }
        return random.nextInt(to - from + 1) + from;
    }

    public static double randomDouble(int from, int to) {
        return random.nextDouble() * (to - from) + from;
    }

    public static double randomDouble(double from, double to) {
        if (from == to) {
            return to;
        }
        return random.nextDouble() * (to - from) + from;
    }

    public static double randomDouble() {
        return random.nextDouble();
    }

    public static int[] randomIntegers(int count, int from, int to) {
        int[] numbers = new int[count];
        for (int i = 0; i < count; i++) {
            numbers[i] = randomInteger(from, to);
        }
        return numbers;
    }

    public static int[] randomDistinctIntegers(int count, int from, int to) {
        Set<Integer> numbers = new HashSet<>();
        while (numbers.size() < count) {
            int number = randomInteger(from, to);
            numbers.add(number);
        }
        List<Integer> list = new ArrayList<>(numbers);
        Collections.shuffle(list);
        int[] array = new int[count];
        Integer[] a = list.toArray(new Integer[count]);
        for (int i = 0; i < count; i++) {
            array[i] = a[i];
        }
        return array;
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

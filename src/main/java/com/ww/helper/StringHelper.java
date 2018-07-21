package com.ww.helper;

public class StringHelper {

    public static String replaceAllNonAlphaumeric(String s){
        return s.replaceAll("[^\\p{IsAlphabetic}^\\p{IsDigit}]", "");
    }
}

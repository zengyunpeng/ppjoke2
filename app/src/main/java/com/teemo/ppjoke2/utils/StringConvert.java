package com.teemo.ppjoke2.utils;

public class StringConvert {

    public static String convertFeedUgc(int count) {
        if (count < 10000) {
            return String.valueOf(count);
        }
        return count / 10000 + "万";
    }
}

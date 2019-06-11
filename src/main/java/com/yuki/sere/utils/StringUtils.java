package com.yuki.sere.utils;

import java.util.Arrays;

/**
 * Created by fan on 2019/6/11.
 */
public class StringUtils {

    public static boolean isNull(String data) {
        return data == null || "".equals(data);
    }

    public static boolean areNull(String... data) {
        return Arrays.stream(data).anyMatch(StringUtils::isNull);
    }
}

package com.dragonchang.util;

import java.util.regex.Pattern;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2024-03-11 17:57
 **/
public class StringUtil {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");
    public static boolean isNumeric2(String str) {
        return str != null && NUMBER_PATTERN.matcher(str).matches();
    }
}

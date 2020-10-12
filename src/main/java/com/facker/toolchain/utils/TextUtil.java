package com.facker.toolchain.utils;

public class TextUtil {
    public static boolean isEmpty(String text) {
        if (text != null && !"".equals(text)) {
            return false;
        }
        return true;
    }
}

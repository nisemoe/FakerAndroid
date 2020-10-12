package com.facker.toolchain.base.shell.api.xbase;

public class TextUtil {
    public static boolean isEmpty(String text) {
        if (text != null && !"".equals(text)) {
            return false;
        }
        return true;
    }
}

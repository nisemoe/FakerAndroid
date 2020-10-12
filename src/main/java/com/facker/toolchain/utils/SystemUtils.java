package com.facker.toolchain.utils;

public class SystemUtils {

    public static boolean isWindows(){
        return System.getProperty("os.name").toLowerCase().indexOf("windows") != -1;
    }
}

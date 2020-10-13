package com.facker.toolchain;

public class FakerMain {
    static String apkFilePath = "your apk file path";
    public static void main(String[] args) {
        FakerTransfer.translate(apkFilePath);
    }
}

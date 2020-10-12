package com.facker.toolchain.base.shell.api.xbase;

import brut.androlib.Androlib;
import brut.androlib.ApkDecoder;
import brut.common.BrutException;
import com.facker.toolchain.base.shell.api.Logger;
import java.io.File;
import java.util.HashMap;

public class ApkTool {
    /**
     * 拆开文件
     *
     * @return
     */
    public static boolean decodeRes(File targetApkPath, File ouputDir) {
        ApkDecoder decoder = new ApkDecoder();
        try {
            decoder.setDecodeSources((short) 0);
            decoder.setOutDir(ouputDir);
            decoder.setForceDelete(true);
            decoder.setApkFile(targetApkPath);
            decoder.decode();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean decodeSrc(File targetApkPath, File ouputDir) {
        Logger.log("targetApkPath path " + targetApkPath.getAbsolutePath());
        ApkDecoder decoder = new ApkDecoder();
        try {
            decoder.setBaksmaliDebugMode(false);
            decoder.setOutDir(ouputDir);
            decoder.setForceDelete(true);
            decoder.setApkFile(targetApkPath);
            decoder.decode();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Android 重新打包
     *
     * @return
     */
    public static boolean build(File sourceDir, File outPutApkPath) {
        Androlib instance = new Androlib();
        HashMap<String, Boolean> flags = new HashMap<String, Boolean>();
        flags.put("forceBuildAll", Boolean.valueOf(true));
        flags.put("debug", Boolean.valueOf(false));
        flags.put("verbose", Boolean.valueOf(false));
        flags.put("framework", Boolean.valueOf(false));
        flags.put("update", Boolean.valueOf(false));
        flags.put("copyOriginal", Boolean.valueOf(false));
        try {
            instance.build(sourceDir, outPutApkPath);
            return true;
        } catch (BrutException e) {
            e.printStackTrace();
        }
        return false;
    }
}

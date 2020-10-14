package com.facker.toolchain.base.shell.api.xbase;
import brut.androlib.meta.MetaInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class YmlMod {

    public static void main(String[] args) throws IOException {
        String yamlStr = "t.yml";
        MetaInfo metaInfo =  MetaInfo.load(new FileInputStream(new File(yamlStr)));
        int ov = Integer.parseInt(metaInfo.versionInfo.versionCode)+1000;
        metaInfo.versionInfo.versionCode =ov+"";
        metaInfo.save(new File(yamlStr));
    }

    public static void modVersionCode(File apktoolYaml,String versionCode) throws IOException {
        MetaInfo metaInfo = null;
        metaInfo = MetaInfo.load(new FileInputStream(apktoolYaml));
        // int ov = Integer.parseInt(metaInfo.versionInfo.versionCode)+inc;
        metaInfo.versionInfo.versionCode =versionCode;
        metaInfo.save(apktoolYaml);
    }
    public static void modVersionName(File apktoolYaml,String versionName) throws IOException {
        MetaInfo metaInfo = null;
        metaInfo = MetaInfo.load(new FileInputStream(apktoolYaml));
        // int ov = Integer.parseInt(metaInfo.versionInfo.versionCode)+inc;
        metaInfo.versionInfo.versionName =versionName;
        metaInfo.save(apktoolYaml);
    }
    public static String getVersionCode(File apktoolYaml){
        MetaInfo metaInfo = null;
        try {
            metaInfo = MetaInfo.load(new FileInputStream(apktoolYaml));
            String  versionCode = metaInfo.versionInfo.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getVersionName(File apktoolYaml){
        MetaInfo metaInfo = null;
        try {
            metaInfo = MetaInfo.load(new FileInputStream(apktoolYaml));
            String  versionName = metaInfo.versionInfo.versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


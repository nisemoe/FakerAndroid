package com.facker.toolchain.test;
import com.facker.toolchain.utils.*;
import com.luhuiguo.chinese.ChineseUtils;
import com.luhuiguo.chinese.pinyin.PinyinFormat;
import java.io.File;

public class SplitApksMerge {
    static String outPutDir = "";
    static final String KEY_PATH = "";
    static String dir = "";
    public static void main(String[] args) throws Exception {
        //合并split apk
    }

    public static String SplitApksMerge(String dir,String outPutDir,String outName) throws Exception {
        File fileDir = new File(dir);
        File apks[] = fileDir.listFiles();

        File base = null;
        for (File apk: apks) {
            if(apk.getName().endsWith(".apk")){
                File decodeDir = new File(apk.getParent(),apk.getName().replace(".apk",""));
                ApkTool.decodeRes(apk,decodeDir);
                File dexCheck = new File(decodeDir,"classes.dex");
                if(dexCheck.exists()){
                    base =decodeDir;
                }
            }
        }
        Logger.log("解压完毕------------");
        //找到主文件夹
        apks = fileDir.listFiles();
        for (File apk: apks){
            if(apk.isDirectory()){
                if(!apk.getName().equals(base.getName())){
                    IOUtil.copyDir(apk,base,false);
                }
            }
        }
        Logger.log("文件合包完毕------------");
        //重新构建
        File huluwa = new File(outPutDir, ChineseUtils.toPinyin(outName, PinyinFormat.TONELESS_PINYIN_FORMAT).replace(" ","-"));
        File mereApk = new File(huluwa,outName+".apk");
        ApkTool.build(base,mereApk);

        //签名
        //SignUtil.sign(mereApk.getAbsolutePath(),KEY_PATH, "cn", "123", "123");
        Logger.log("构建完毕------------");
        for (File apk: apks){
            if(apk.isDirectory()){
                FileUtil.deleteDir(apk);
            }
        }
        Logger.log("删除残留文件完毕------------");
        return "";
    }
}

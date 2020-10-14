package com.facker.toolchain.base.mod;


import com.facker.toolchain.base.utils.SignUtil;
import com.facker.toolchain.utils.Logger;
import com.facker.toolchain.api.xbase.ApkTool;

import java.io.File;


public class DealApk {
    private static final String IN_DIR = Constant.PATH.ROOT_DIR +File.separator+"apkin";
    public static void main(String[] args) {
//
    }
    static void multiFuck(){
        File file =new File("D:\\crazy\\apkin");
        File xApks[] = file.listFiles();
        for (File xApk:xApks) {
            String name = xApk.getName();
            try{
                fuckApk(name,false,false,false,true);
            }catch (Exception e){
                e.printStackTrace();;
            }
        }
    }
    static void fuckApk(String apkName,boolean coverSource,boolean rmAds,boolean rmGoolge,boolean rmFileProvider){
        String filePath  = makeCleaSource(apkName,coverSource);
        if(rmGoolge){
            StaticInjector.coverGoogleDialog(filePath);
        }
        if(rmAds){
            StaticInjector.rmAds(filePath);
        }
        if(rmFileProvider){
            StaticInjector.rmFileProvider(filePath);
        }
        buildCleaApk(filePath);
    }
    static String makeCleaSource(String apkName,boolean coverSource){
        String apkPath = IN_DIR+File.separator+apkName;
        String sourcePath = Constant.PATH.SOURCE_PERFECT + File.separator + apkName.replace(".apk","");
        File cF = new File(sourcePath);
        if(cF.exists()&&!coverSource){
            return sourcePath;
        }
        ApkTool.decodeSrc(new File(apkPath), new File(sourcePath));
        return sourcePath;
    }
    static void buildCleaApk(String sourcePath){
        File s = new File(sourcePath);
        String name = s.getName();
        name = name.replace(" ","");
        String targetApkPath = Constant.PATH.SOURCE_PERFECT_APK + File.separator +name +".apk";
        ApkTool.build(new File(sourcePath), new File(targetApkPath));
        SignUtil.sign(targetApkPath,Constant.PATH.CONFIG_INJECTOR_VAPP, "cn","vapp123", "vapp123");
        Logger.log("安装签名完成---------,开始执行安装操作");
    }
}

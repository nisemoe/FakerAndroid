package com.facker.toolchain.test;

import brut.androlib.AndrolibException;
import brut.androlib.meta.MetaInfo;
import brut.directory.ExtFile;
import com.facker.toolchain.test.MSmaliBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SmaliTest {

    public static void main(String[] args) {

        String rootPath = "C:\\Users\\Yang\\Desktop\\apk\\juan-juan-bing-qi-lin\\juan-juan-bing-qi-lin\\app\\src\\main\\smalis";
        File smaliDirs[] = new File(rootPath).listFiles();
        File apktoolYaml = new File("C:\\Users\\Yang\\Desktop\\apk\\juan-juan-bing-qi-lin\\juan-juan-bing-qi-lin\\app\\src\\main\\apktool.yml");
        File javaSrc = new File("C:\\Users\\Yang\\Desktop\\apk\\juan-juan-bing-qi-lin\\juan-juan-bing-qi-lin\\app\\src\\main\\java");
        MetaInfo metaInfo = null;
        try {
            metaInfo = MetaInfo.load(new FileInputStream(apktoolYaml));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for ( int i=0;i<smaliDirs.length;i++){
            File f = smaliDirs[i];
            if(f.isDirectory()&&f.getName().startsWith("smali")&&!f.getName().endsWith("assets")){
                ExtFile extFile = new ExtFile(f);
                try {
                    String endStuff = null;
                    if(i==0){
                        endStuff = "_fk.dex";
                    }else {
                        endStuff = i+"_fk.dex";
                    }
                    int targetSdkVersion = Integer.parseInt(metaInfo.sdkInfo.get("targetSdkVersion"));
                    System.out.println("targetSdkVersion "+targetSdkVersion);
                    MSmaliBuilder.build(extFile,new File("C:\\Users\\Yang\\Desktop\\apk\\juan-juan-bing-qi-lin\\juan-juan-bing-qi-lin\\app\\src\\main\\assets\\classes"+endStuff),targetSdkVersion,javaSrc);
                } catch (AndrolibException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

package com.facker.toolchain.base.beta;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TestClassify {

    public static void main(String[] args) throws IOException {
        File dir = PathUtil.inputFile("", JFileChooser.FILES_AND_DIRECTORIES, false);
        int u3dCount = 0, cocos2dCount = 0, unknowCount = 0, ueCount = 0;
        for (File file : dir.listFiles()) {
            String target = "unknow";
            if(file.isFile() && file.getName().endsWith(".apk")){
                ZipFile z = new ZipFile(file);
                Enumeration<? extends ZipEntry> zz =  z.entries();
                while (zz.hasMoreElements()){
                    ZipEntry entry = zz.nextElement();
                    if(entry.getName().contains("cocos2d.so")){
                        target = "cocos2d";
                    }else if (entry.getName().contains("unity.so")||
                            entry.getName().contains("assets\\bin\\Data\\Managed")){
                        target = "unity3d";
                    }else if(entry.getName().contains("UnrealEngine")) {
                        target = "ue";
                    }
                }
                switch (target){
                    case "unknow":
                        unknowCount++;
                        break;
                    case "cocos2d":
                        cocos2dCount++;
                        break;
                    case "unity3d":
                        u3dCount ++;
                        break;
                    case "ue":
                        ueCount++;
                        break;
                }
                z.close();
                System.out.printf("%s Type:%s\n", file.getName(), target);
            }
        }
        System.out.printf("U3D:%d, Cocos:%d, ue:%d, unkonw:%d", u3dCount, cocos2dCount, ueCount, unknowCount);
    }

}

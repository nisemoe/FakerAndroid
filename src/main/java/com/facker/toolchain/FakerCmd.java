package com.facker.toolchain;

import java.io.File;

public class FakerCmd {
    public static void main(String[] args) {
        if(args.length>0){
            String path = args[0];
            File file = new File(path);
            if(file.exists()){
                if(!file.getName().endsWith(".apk")){
                    System.out.println("非法的Apk文件路径");
                }else {
                    FakerTransfer.translate(path);
                }
            }else {
                System.out.println("Apk文件路径不正确");
            }
        }else {
            System.out.println("请输入apk文件路径");
        }

    }
}

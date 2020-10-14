package com.facker.toolchain.api.xbase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DecodeApk {

    private File originalFile;

    private String md5;

    private long id;

    private static String DEX_MATCHER = "classes[1-9]?\\d*\\.dex";


    public long getId() {
        return id;
    }

    public DecodeApk(long id,File originalFile,String md5) {
        this.originalFile = originalFile;
        this.md5 = md5;
        this.id = id;
    }

    public File getOriginalFile() {
        return originalFile;
    }
    /**
     * 获取解压目录
     * @return
     */
    public File getDecodeDir() {
        File file = new File(originalFile.getParent(),getClass().getSimpleName()+"_"+getId()+"_"+md5);
        if(!file.exists()){
            file.mkdir();
        }
        return file;
    }

    public File getMETAINF() {
        File file = new File(getDecodeDir(),"META-INF");
        return file;
    }
    public File getAssets() {
        File file = new File(getDecodeDir(),"assets");
        if(!file.exists()){
            file.mkdir();
        }
        return file;
    }

    public File getLibDir() {
        File file = new File(getDecodeDir(),"lib");
        return file;
    }

    public File getXMLDir() {
        File file = new File(getResDir(),"xml");
        return file;
    }

    public File getResDir() {
        File file = new File(getDecodeDir(),"res");
        if(!file.exists()){
            file.mkdir();
        }
        return file;
    }

    public File getManifestFile() {
        File file = new File(getDecodeDir(),"AndroidManifest.xml");
        return file;
    }

    public File getYmlFile() {
        File file = new File(getDecodeDir(),"apktool.yml");
        return file;
    }

    public List<File> getDexsFiles() {
        List<File> targetDexList = new ArrayList<File>();
        for (File f : getDecodeDir().listFiles()) {
            if (f.getName().matches(DEX_MATCHER)) {
                targetDexList.add(f);
            }
        }
        return targetDexList;
    }

    public File getMainDexFile() {
        File file = new File(getDecodeDir(),"classes.dex");
        return file;
    }

    public boolean decode(){
        /**
         * 缓存逻辑判断
         */
        return ApkTool.decodeRes(originalFile,getDecodeDir());
    }
}

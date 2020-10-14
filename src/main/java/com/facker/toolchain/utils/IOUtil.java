package com.facker.toolchain.utils;

import java.io.*;
import java.util.zip.ZipOutputStream;

public class IOUtil {

    public static void copy(InputStream is, OutputStream os) throws IOException {
        int len;
        final OutputStream outputStream = os instanceof ZipOutputStream ? os : new BufferedOutputStream(os);
        final BufferedInputStream inputStream =  new BufferedInputStream(is);
        byte[] bytes = new byte[10240];
        while ((len = inputStream.read(bytes)) != -1){
            outputStream.write(bytes, 0, len);
        }
        inputStream.close();
        if(os instanceof ZipOutputStream){
            ((ZipOutputStream) os).closeEntry();
        }else {
            outputStream.close();
        }
    }

    public static void copy(File i, File o) throws IOException {
        copy(new FileInputStream(i), new FileOutputStream(o));
    }
    
    public static void copyDir(String oldPath, String newPath) throws IOException {
        File file = new File(oldPath);
        //文件名称列表
        String[] filePath = file.list();
        
        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdir();
        }
        
        for (int i = 0; i < filePath.length; i++) {
            if ((new File(oldPath + File.separator + filePath[i])).isDirectory()) {
                copyDir(oldPath  + File.separator  + filePath[i], newPath  + File.separator + filePath[i]);
            }
            if (new File(oldPath  + File.separator + filePath[i]).isFile()) {
                copyFile(oldPath + File.separator + filePath[i], newPath + File.separator + filePath[i]);
            }
        }
    }
    public static void copyDir(String oldPath, String newPath,boolean cover) throws IOException {
        File file = new File(oldPath);
        //文件名称列表
        String[] filePath = file.list();

        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdir();
        }

        for (int i = 0; i < filePath.length; i++) {
            if ((new File(oldPath + File.separator + filePath[i])).isDirectory()) {
                copyDir(oldPath  + File.separator  + filePath[i], newPath  + File.separator + filePath[i]);
            }
            if (new File(oldPath  + File.separator + filePath[i]).isFile()) {
                copyFile(oldPath + File.separator + filePath[i], newPath + File.separator + filePath[i],cover);
            }
        }
    }
    
    public static void copyDir(File old, File mnew) throws IOException {
        copyDir(old.getAbsolutePath(),mnew.getAbsolutePath());
    }

    public static void copyDir(File old, File mnew,boolean cover) throws IOException {
        copyDir(old.getAbsolutePath(),mnew.getAbsolutePath(),cover);
    }

    public static void copyFile(String oldPath, String newPath) throws IOException {
    	copy(new FileInputStream(oldPath), new FileOutputStream(newPath));
    }

    public static void copyFile(String oldPath, String newPath,boolean cover) throws IOException {
        File file = new File(newPath);
        if(file.exists()){
            if(!cover){
                return;
            }
        }
        copy(new FileInputStream(oldPath), new FileOutputStream(newPath));
    }
    
    public static void copyFile(File oldFile, File newFile) throws IOException {
    	copy(new FileInputStream(oldFile), new FileOutputStream(newFile));
    }

    public static void copyRootPng(File o,File t){
        File files[] = o.listFiles();
        for (File f: files) {
            String name= f.getName();
            Logger.log("----"+name);
            if(name.endsWith(".png")||name.endsWith(".jpg")){
                try {
                    IOUtil.copyFile(new File(o.getAbsolutePath(),name),new File(t.getAbsolutePath(),name));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

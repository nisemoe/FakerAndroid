package com.facker.toolchain.utils;
import java.io.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

public class FileUtil {

    public static void autoReplaceStr(File file, String oldstr, String newStr) throws IOException {
        Long fileLength = file.length();
        byte[] fileContext = new byte[fileLength.intValue()];
        FileInputStream in = null;
        PrintWriter out = null;
        in = new FileInputStream(file);
        in.read(fileContext);
        String str = new String(fileContext, "utf-8");//字节转换成字符
        str = str.replace(oldstr, newStr);
        out = new PrintWriter(file, "utf-8");//写入文件时的charset
        out.write(str);
        out.flush();
        out.close();
        in.close();
    }
    /**
     * 循环删除目录
     * @param dir
     * @return
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
//            dir.delete();
        }
        return dir.delete();
    }
    
    public static void coverFile(String filePath, String str) throws IOException {
		 FileOutputStream fos=new FileOutputStream(filePath,false);
	     fos.write(str.getBytes());
	     fos.close();      
	}
    
    public static void writeFile(String path,String str) throws IOException {
        File fout = new File(path);
        FileOutputStream fos = new FileOutputStream(fout,true);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write(str);
        bw.newLine();
        bw.close();
    }

    public static long calcFileCRC32(File file) throws IOException {
        FileInputStream fi = new FileInputStream(file);
        CheckedInputStream checksum = new CheckedInputStream(fi, new CRC32());
        while (checksum.read() != -1) { }
        long temp = checksum.getChecksum().getValue();
        fi.close();
        checksum.close();
        return temp;
    }
}

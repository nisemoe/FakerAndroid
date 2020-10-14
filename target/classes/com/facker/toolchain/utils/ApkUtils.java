package com.facker.toolchain.utils;

import com.google.common.base.Charsets;

import java.io.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ApkUtils {

    public static AaptApk read(File file) throws Exception{//TODO TEST
        final String aaptUtil = SystemUtils.isWindows() ? "aapt.exe" : "aapt";//.exe .exe
        String exec[] = new String[]{ aaptUtil, "dump", "badging", file.getAbsolutePath() };
        Process process = Runtime.getRuntime().exec(exec);
        final String result = convert(process.getInputStream(), Charsets.UTF_8);
        return new AaptApk(result, process.exitValue());
    }

    private static String convert(InputStream inputStream, Charset charset) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\r\n");
            }
        }
        return stringBuilder.toString();
    }

    public static class FileInfo{
        public final String name;
        public final File file;

        public FileInfo(String name, File file) {
            this.name = name;
            this.file = file;
        }
    }

    public static FileInfo extractTempFile(File zip, String key) throws IOException, NoSuchAlgorithmException {
        ZipFile zipFile = new ZipFile(zip);
        Enumeration<? extends ZipEntry> entries =  zipFile.entries();
        while (entries.hasMoreElements()){
            final ZipEntry entry = entries.nextElement();
            if(entry.getName().equals(key)){
                MessageDigest md = MessageDigest.getInstance("MD5");
                InputStream i = zipFile.getInputStream(entry);

                File file = File.createTempFile("icon", ".png");

                FileOutputStream stream = new FileOutputStream(file);

                byte[] data = new byte[4096];
                int count = 0;
                while ((count = i.read(data)) != -1){
                    md.update(data, 0, count);
                    stream.write(data, 0, count);
                }
                i.close();
                stream.flush();
                stream.close();
                final String name = byteArrayToHex(md.digest());
                final File renameFile = File.createTempFile(name, ".png");

                IOUtil.copy(file, renameFile);
                file.delete();

                return new FileInfo(name, renameFile);
            }
        }
        return null;
    }

    /**
     * 通过Key来提取相似的图片文件
     * @param zip
     * @param key
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static FileInfo extractSimilarImageFile(File zip, String key) throws IOException, NoSuchAlgorithmException {
        String keyParts[] = key.split("/");
        String fuzzyEntityName = null;
        String fuzzyFolderType = null;
        if (keyParts.length == 3){
            //Normal
            assert "res".equals(keyParts[0]);
            if(keyParts[1].contains("-")){
                String midParts[] = keyParts[1].split("-");
                fuzzyFolderType = midParts[0];
            }else {
                fuzzyFolderType = keyParts[1];
            }
            if(keyParts[2].contains(".")){
                int lastIndex = keyParts[2].lastIndexOf(".");
                fuzzyEntityName = keyParts[2].substring(0, lastIndex);
            }else {
                fuzzyEntityName = keyParts[2];
            }
        }
//        if(path.startsWith("/res")){
//            path = path.substring(4);
//            if(path.contains("-")){
//            }
//        }else {
//            throw new IOException("No Matched File");
//        }
        ZipFile zipFile = new ZipFile(zip);
        Enumeration<? extends ZipEntry> entries =  zipFile.entries();
        List<ZipEntry> entryList = new ArrayList<>();
        while (entries.hasMoreElements()){
            final ZipEntry entry = entries.nextElement();
            final String entityName = entry.getName();
            boolean startWithRes = entityName.startsWith("res");
            String parts[] = entityName.split("/");
            if(startWithRes && parts.length == 3
                    && parts[1].startsWith(fuzzyFolderType)
                    && parts[2].startsWith(fuzzyEntityName)
                    && (parts[2].endsWith(".png") || parts[2].endsWith(".jpg"))){
                MessageDigest md = MessageDigest.getInstance("MD5");
                InputStream i = zipFile.getInputStream(entry);

                File file = File.createTempFile("icon", ".png");

                FileOutputStream stream = new FileOutputStream(file);

                byte[] data = new byte[4096];
                int count = 0;
                while ((count = i.read(data)) != -1){
                    md.update(data, 0, count);
                    stream.write(data, 0, count);
                }
                i.close();
                stream.flush();
                stream.close();
                final String name = byteArrayToHex(md.digest());
                final File renameFile = File.createTempFile(name, ".png");

                IOUtil.copy(file, renameFile);
                file.delete();
                return new FileInfo(name, renameFile);
            }
        }
        return null;
    }

    public static String md5(File in) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        InputStream i = new FileInputStream(in);

        byte[] data = new byte[4096];
        int count = 0;
        while ((count = i.read(data)) != -1){
            md.update(data, 0, count);
        }
        i.close();
        final String md5 = byteArrayToHex(md.digest());
        return md5;
    }

    public static String md5(byte[] in) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(in, 0, in.length);
        return byteArrayToHex(md.digest());
    }

    private static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    public static AaptApk read(String file) throws Exception{
        return read(new File(file));
    }
}

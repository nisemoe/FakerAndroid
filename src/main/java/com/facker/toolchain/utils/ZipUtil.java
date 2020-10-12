package com.facker.toolchain.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    final ZipFile mFile;
    final Enumeration<? extends ZipEntry> entries;
    Map<String, ZipEntry> mMap = null;

    public ZipUtil(File in) throws IOException {
        this.mFile = new ZipFile(in, Charset.forName("utf-8"));
        this.entries = mFile.entries();
    }

    public void unZip(File out) throws IOException {
        if(!out.exists()){
            out.mkdir();
        }
//        mMap = new HashMap<>();
        while (entries.hasMoreElements()){
            ZipEntry item = entries.nextElement();
//            mMap.put(item.getName(), item);
            File file = new File(out, item.getName());
            int pos = item.getName().lastIndexOf("/");
            File dir = new File(out, pos == -1 ? item.getName() : item.getName().substring(0, pos));
            if(pos != -1){
                dir.mkdirs();
                dir.setWritable(true);
            }
            file.createNewFile();
            IOUtil.copy(mFile.getInputStream(item), new FileOutputStream(file));
            file.setWritable(true);
        }
        mFile.close();
    }


    private static final int BUFFER_SIZE = 2 * 1024;

    public static void toZip(String srcDir, OutputStream out,boolean KeepDirStructure) throws RuntimeException {


        ZipOutputStream zos = null;

        try {

            zos = new ZipOutputStream(out);

            for (File f : new File(srcDir).listFiles()) {
                compress(f.getAbsoluteFile(), zos, f.getName(), KeepDirStructure);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }

            }
        }
    }

    public static void toZip(List<File> srcFiles, File parent, OutputStream out)
            throws RuntimeException {
        int startPos = parent.getAbsolutePath().length();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[BUFFER_SIZE];
                String name = srcFile.getName().startsWith("classes") ?srcFile.getName():srcFile.getAbsolutePath().substring(startPos + 1);
                zos.putNextEntry(new ZipEntry(name));
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void toZip(List<File> srcFiles, OutputStream out) throws RuntimeException {

        ZipOutputStream zos = null;

        try {

            zos = new ZipOutputStream(out);

            for (File srcFile : srcFiles) {

                byte[] buf = new byte[BUFFER_SIZE];

                zos.putNextEntry(new ZipEntry(srcFile.getName()));

                int len;

                FileInputStream in = new FileInputStream(srcFile);

                while ((len = in.read(buf)) != -1) {

                    zos.write(buf, 0, len);

                }

                zos.closeEntry();

                in.close();

            }
        } catch (Exception e) {

            throw new RuntimeException("zip error from ZipUtils", e);

        } finally {

            if (zos != null) {

                try {

                    zos.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }

    }

    private static void  compress(File sourceFile, ZipOutputStream zos,String name,boolean KeepDirStructure) throws Exception {

        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            ZipEntry entry = new ZipEntry(name);
//            if(mMap != null && mMap.containsKey(name)){
//                entry = mMap.get(name);
//            }
//            entry.setSize(sourceFile.length());
//            entry.setCompressedSize(-1);
            zos.putNextEntry(entry);
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                if (KeepDirStructure) {
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    if (KeepDirStructure) {
                        compress(file, zos, name + "/" + file.getName(),KeepDirStructure);
                       
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }
        }
    }
}
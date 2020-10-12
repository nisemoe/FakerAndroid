package com.facker.toolchain.base.shell.api.xbase;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.zip.ZipOutputStream;

public class ResMerge {

    public static void merge(File source, File target) throws IOException, DocumentException {
        copyDir(source.getAbsolutePath(),target.getAbsolutePath());
    }

    private static void copyDir(String oldPath, String newPath) throws IOException, DocumentException {
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
                mereFile(oldPath + File.separator + filePath[i], newPath + File.separator + filePath[i]);
            }
        }
    }
    private static void mereFile(String oldPath, String newPath) throws IOException, DocumentException {
        File oFile = new File(oldPath);
        File targetFile = new File(newPath);
        System.out.println("正在处理文件原始地址"+oldPath);
        System.out.println("正在处理文件"+targetFile.getAbsolutePath());
        if(oFile.getParentFile().getName().startsWith("values")) {
            if(!targetFile.exists()) {//不存在直接拷贝
                copyFile(oldPath,newPath);
            } else {
                if(!oFile.getName().equals("public.xml")) {//非public文件直接追加合并
                    try {
                        ValuesMod oldValuesMod = new ValuesMod(new File(oldPath));
                        ValuesMod newValuesMod = new ValuesMod(new File(newPath));
                        newValuesMod.copyValueElements(oldValuesMod.getResourcesElement(),newValuesMod.getResourcesElement());
                        newValuesMod.save();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }else {//public 文件计算merge
                    meregePublicXml(oFile,targetFile);
                }
            }

        }else if(oFile.getParentFile().getName().startsWith("xml")) {
            //如果是网络配置文件
            String rootTagName = getXmlRootTagName(oFile);
            if("network-security-config".equals(rootTagName)) {   //根据manifest 获取网络配置文件，名称
                ManifestEditor manifestEditor = new ManifestEditor(new File(targetFile.getParentFile().getParentFile().getParentFile(),"AndroidManifest.xml"));
                String networkSecurityConfig = manifestEditor.getNetworkSecurityConfig();
                File targetNetworkSecurityConfigFile = new File(targetFile.getParentFile(),networkSecurityConfig+".xml");
                NetworkSecurityConfigEditor oNetworkSecurityConfigEditor = new NetworkSecurityConfigEditor(oFile);
                NetworkSecurityConfigEditor targetNetworkSecurityConfigEditor = new NetworkSecurityConfigEditor(targetNetworkSecurityConfigFile);
                targetNetworkSecurityConfigEditor.formatElement();
                targetNetworkSecurityConfigEditor.copyElements(oNetworkSecurityConfigEditor.getDomainElements(),targetNetworkSecurityConfigEditor.getDomainConfigElement());
                targetNetworkSecurityConfigEditor.save();
                if(!targetFile.exists()){
                    copyFile(oldPath,newPath);
                }
            }else if("paths".equals(rootTagName)){
                if(!targetFile.exists()){
                    copyFile(oldPath,newPath);
                }else{
                    PathEditor oldPathEditor = new PathEditor(oFile);
                    PathEditor newPathEditor = new PathEditor(oFile);
                    newPathEditor.copyElements(oldPathEditor.getResourcesElement(),newPathEditor.getResourcesElement());
                    newPathEditor.save();
                }
            }else {
                if(!targetFile.exists()){
                    copyFile(oldPath,newPath);
                }
            }
            //如果是path文件
        }else{
            if(!targetFile.exists()&&!nameRepeat(newPath)){
                copyFile(oldPath,newPath);
            }else{
                //保留原来的文件
            }
        }
    }

    private static boolean nameRepeat(String path){
        File file = new File(path);
        String name = file.getName();
        String noExtName = getFileNameNoEx(name);
        File dir = file.getParentFile();
        File []  files = dir.listFiles();
        for (File f: files) {
            if(noExtName.equals(getFileNameNoEx(f.getName()))){
                return true;
            }
        }
        return false;
    }

    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    private static String  getXmlRootTagName(File file){
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element resourcesElement = document.getRootElement();
        return resourcesElement.getName();
    }
    private static void  meregePublicXml(File oldFile, File newFile) throws DocumentException {
        PublicMerge.mergePublicXml(oldFile,newFile);
    }

    private static void copyFile(String oldPath, String newPath) throws IOException {
        copy(new FileInputStream(oldPath), new FileOutputStream(newPath));
    }

    private static void copy(InputStream is, OutputStream os) throws IOException {
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

}

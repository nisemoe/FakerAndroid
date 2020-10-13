package com.facker.toolchain.base.shell.api.xbase;
import brut.androlib.meta.MetaInfo;
import com.facker.toolchain.base.shell.api.Logger;
import com.facker.toolchain.utils.FileUtil;
import com.facker.toolchain.utils.IOUtil;
import org.dom4j.DocumentException;

import java.io.*;
import java.util.HashMap;

public class Importer extends IImporter {

    public Importer(XSrcTarget xSrcTarget, SourceCode sourceCode) {
        super(xSrcTarget, sourceCode);
    }
    @Override
    boolean unZipTarget() {
        return xSrcTarget.decode();
    }

    @Override
    boolean orlderXTarget(XSrcTarget xSrcTarget) throws IOException {
        Logger.log(xSrcTarget.getLibDir().getAbsolutePath());
        //so
        File libDir = xSrcTarget.getLibDir();
        libDir.renameTo(xSrcTarget.getjniLibs());

        //java
        File javaDir = xSrcTarget.getJava();
        javaDir.mkdirs();

        //libs
        File libs = xSrcTarget.getLibs();
        libs.mkdir();

        //cpp
        File cpp = xSrcTarget.getCpp();
        cpp.mkdir();

        //smali
        File smalis = xSrcTarget.getSmalis();
        smalis.mkdirs();
        File files[] = xSrcTarget.getDecodeDir().listFiles();
        for (File f:files) {
            if(f.isDirectory()&&f.getName().startsWith("smali")){
                f.renameTo(new File(smalis,f.getName()));
            }
        }
        return true;
    }

    @Override
    boolean mergeSourceCode(SourceCode sourceCode, XSrcTarget xSrcTarget) throws IOException {
        //拷贝cpp
        IOUtil.copyDir(sourceCode.getCpp(),xSrcTarget.getCpp());

        //拷贝Java
        IOUtil.copyDir(sourceCode.getJava(),xSrcTarget.getJava());

        //delete(xSrcTarget.getResDir());

        try {
            ResMerge.merge(sourceCode.getRes(),xSrcTarget.getResDir());
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        IOUtil.copyDir(sourceCode.getBuildGame(),xSrcTarget.getGameDir());

        //TODO 修复build gradle
        File gameBuildGrandle = xSrcTarget.getGameBuild();

        try {
            ManifestEditor manifestEditor = new ManifestEditor(xSrcTarget.getManifestFile());
            FileUtil.autoReplaceStr(gameBuildGrandle,"{pkg}",manifestEditor.getPackagenName());

            File MainA = new File(xSrcTarget.getJava(),"com\\faker\\android\\FakerUnityActivity.java");
            FileUtil.autoReplaceStr(MainA,"{R}",manifestEditor.getPackagenName()+".R");

            File MainW = new File(xSrcTarget.getJava(),"com\\faker\\android\\WebViewActivity.java");
            FileUtil.autoReplaceStr(MainW,"{R}",manifestEditor.getPackagenName()+".R");

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        MetaInfo metaInfo = null;
        try {
            metaInfo = MetaInfo.load(new FileInputStream(xSrcTarget.getYmlFile()));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        FileUtil.autoReplaceStr(gameBuildGrandle,"{versionCode}",metaInfo.versionInfo.versionCode);
        FileUtil.autoReplaceStr(gameBuildGrandle,"{versionName}",metaInfo.versionInfo.versionName);
        String minSdkVersion = metaInfo.sdkInfo.get("minSdkVersion");
        //FileUtil.autoReplaceStr(gameBuildGrandle,"{minSdkVersion}",minSdkVersion);
        String targetSdkVersion =metaInfo.sdkInfo.get("targetSdkVersion");
        FileUtil.autoReplaceStr(gameBuildGrandle,"{targetSdkVersion}",targetSdkVersion);



        IOUtil.copyDir(sourceCode.getBuildProject(),xSrcTarget.getProjectDir());

        IOUtil.copyDir(sourceCode.getBuildJavaScffoing(),xSrcTarget.getJavaScaffoding());

        return true;
    }

    @Override
    boolean makeCppScaffolding (XSrcTarget xSrcTarget) throws IOException {
        exportCppScaffolding(xSrcTarget);
        //整理脚手架
        File scaffolding_ARM = new File(xSrcTarget.getCpp(),"Scaffolding-ARM");
        formatScaffolding(scaffolding_ARM);

        File Scaffolding_ARM64 = new File(xSrcTarget.getCpp(),"Scaffolding-ARM64");
        formatScaffolding(Scaffolding_ARM64);

        return true;
    }

    @Override
    boolean makeJavaScaffolding(SourceCode sourceCode, XSrcTarget xSrcTarget) throws IOException {

        File file = xSrcTarget.getJavaScaffoding();
        if(!file.exists()){
            file.mkdir();
        }
        File fileJavaScaffodingJava = xSrcTarget.getJavaScaffodingJava();
        if(!fileJavaScaffodingJava.exists()){
            fileJavaScaffodingJava.mkdirs();
        }
        File xSrcTargetJavaScaffodingLibs = xSrcTarget.getJavaScaffodingLibs();
        if(!xSrcTargetJavaScaffodingLibs.exists()){
            xSrcTargetJavaScaffodingLibs.mkdir();
        }
        IOUtil.copyDir(sourceCode.getJavaScaffodingLibs(),xSrcTargetJavaScaffodingLibs);

        IOUtil.copyDir(sourceCode.getJavaScaffodingJava(),fileJavaScaffodingJava);

        IOUtil.copyDir(sourceCode.getJavaScaffodingJava(),fileJavaScaffodingJava);

        IOUtil.copyDir(sourceCode.ManifestjavaScaffoding(),xSrcTarget.getJavaScaffodingMain());

        return true;
    }

    @Override
    boolean mergeFaker(SourceCode sourceCode, XSrcTarget xSrcTarget) throws IOException {
        File targetjniLibs = xSrcTarget.getjniLibs();
        File jniLibsARMV7A = new File(targetjniLibs,"armeabi-v7a");
        if(jniLibsARMV7A.exists()){
            IOUtil.copyDir(new File(sourceCode.getJniLibs(),"armeabi-v7a"),jniLibsARMV7A);
        }
        File jniLibsARM64V8A = new File(targetjniLibs,"arm64-v8a");
        if(jniLibsARM64V8A.exists()){
            IOUtil.copyDir(new File(sourceCode.getJniLibs(),"armeabi-v7a"),jniLibsARM64V8A);
        }
        return true;
    }

    @Override
    boolean modManifest(SourceCode sourceCode, XSrcTarget xSrcTarget) throws IOException {
        try {
            ManifestEditor manifestEditor = new ManifestEditor(xSrcTarget.getManifestFile());
            String applicationName = manifestEditor.getApplicationName();
            File file  = new File (xSrcTarget.getJava(),"com\\faker\\android\\FakerApp.java");
            if(!TextUtil.isEmpty(applicationName)){
                FileUtil.autoReplaceStr(file,"{APPLICATION_NAME}",applicationName);
            }else {
                FileUtil.autoReplaceStr(file,"{APPLICATION_NAME}","");
            }
            manifestEditor.modApplication("com.faker.android.FakerApp");//动态
            manifestEditor.save();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    boolean fixRes(SourceCode sourceCode, XSrcTarget xSrcTarget) throws IOException {

        File fileRes = xSrcTarget.getResDir();
        func(fileRes,fileRes);


        return true;
    }
    private static void func(File res,File file){
        File[] fs = file.listFiles();
        for(File f:fs){
            if(f.isDirectory())	//若是目录，则递归打印该目录下的文件
                func(res,f);
            if(f.isFile()){
                System.out.println(f);
                if(f.getName().startsWith("$")){
                    File fixName = new File(f.getParent(),f.getName().replace("$",""));
                    funcRe(res,getFileNameNoEx(f.getName()),getFileNameNoEx(fixName.getName()));
                    f.renameTo(fixName);
                }
            }
        }
    }
    /*
     * Java文件操作 获取不带扩展名的文件名
     * */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
    private static void funcRe(File file,String oStr,String nStr){
        File[] fs = file.listFiles();
        for(File f:fs){
            if(f.isDirectory())	//若是目录，则递归打印该目录下的文件
                funcRe(f,oStr,nStr);
            if(f.isFile()){
                try {
                    if(f.getName().endsWith(".xml")){
                        FileUtil.autoReplaceStr(f,oStr,nStr);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void formatScaffolding(File scaffolding) throws IOException {
        if(scaffolding.exists()&&scaffolding.isDirectory()){
            File scaffolding_ARM_spam[] =scaffolding.listFiles();

            for (File f:scaffolding_ARM_spam ) {//删除无用文件
                if(!f.isDirectory()){
                    f.delete();
                }
            }
            File userDir = new File(scaffolding,"user");
            delete(userDir);

            File frameworkDir = new File(scaffolding,"framework");
            delete(frameworkDir);
            File appdataDir = new File(scaffolding,"appdata");
            File appdataFils[] = appdataDir.listFiles();
            for (File f:appdataFils) {
                f.renameTo(new File(scaffolding,f.getName()));
            }
            delete(appdataDir);
            IOUtil.copyFile(new File(sourceCode.getScaffolding_cpp(),"il2cpp-init.cpp"),new File(scaffolding,"il2cpp-init.cpp"));
            IOUtil.copyFile(new File(sourceCode.getScaffolding_cpp(),"il2cpp-init.h"),new File(scaffolding,"il2cpp-init.h"));
            IOUtil.copyFile(new File(sourceCode.getScaffolding_cpp(),"il2cpp-appdata.h"),new File(scaffolding,"il2cpp-appdata.h"));
            IOUtil.copyFile(new File(sourceCode.getScaffolding_cpp(),"MonoString.h"),new File(scaffolding,"MonoString.h"));
            IOUtil.copyFile(new File(sourceCode.getScaffolding_cpp(),"MonoString.cpp"),new File(scaffolding,"MonoString.cpp"));


            File file1 = new File(xSrcTarget.getDecodeDir(),"kotlin");
            delete(file1);
            File file2 = new File(xSrcTarget.getDecodeDir(),"META-INF");
            delete(file2);

            File file3 = new File(xSrcTarget.getDecodeDir(),"original");
            delete(file3);

            File file4 = new File(xSrcTarget.getDecodeDir(),"unknown");
            delete(file4);
        }
    }


    public void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if(f.getName().startsWith("mipmap")){
                      continue;
                }
                if(f.getName().equals("xml")){
                    continue;
                }

                delete(f);
             }
        }
        if(!file.getName().equals("strings.xml")&&!file.getName().equals("integers.xml")&&!file.getName().equals("public.xml")){
            file.delete();
        }
    }

    public static void exportCppScaffolding (XSrcTarget xSrcTarget) {


        File fileScaffoldingHelper =  new File(xSrcTarget.getCpp(),"ScaffoldingHelper");
        fileScaffoldingHelper.mkdir();

        String cmd = "cmd/Il2Cpp.exe  -i "+xSrcTarget.getOriginalApkFile().getAbsolutePath()+ " -h "
                +new File(xSrcTarget.getCpp(),"Scaffolding").getAbsolutePath()+" -e none -c "+new File(fileScaffoldingHelper,"help.cs").getAbsolutePath()
                +" -p "+new File(fileScaffoldingHelper,"help.py").getAbsolutePath() +" -o" +new File(fileScaffoldingHelper,"help.json").getAbsolutePath();
        BufferedReader br = null;
        BufferedReader brError = null;
        try {
            //执行exe  cmd可以为字符串(exe存放路径)也可为数组，调用exe时需要传入参数时，可以传数组调用(参数有顺序要求)
            Process p = Runtime.getRuntime().exec(cmd);
            String line = null;
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            brError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((line = br.readLine()) != null  || (line = brError.readLine()) != null) {
                //输出exe输出的信息以及错误信息
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

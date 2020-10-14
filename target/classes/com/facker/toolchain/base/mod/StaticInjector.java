package com.facker.toolchain.base.mod;

import com.facker.toolchain.base.utils.IOUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StaticInjector {
    public static final File NoneDialog = new File("NoneDialog.smali");
    public static  boolean copyDepends2TargetDir(File out) throws IOException {
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(NoneDialog));
        File dir = new File(out,"smali/com/google/android/gms/common/");
        if(!dir.exists()){
            dir.mkdirs();
        }
        File noneDialog = new File(out, "smali/com/google/android/gms/common/NoneDialog.smali");
        if(noneDialog.exists()){
            return true;
        }else{

        }
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(noneDialog));
        IOUtil.copy(inputStream, stream);
        return false;
    }

    public static void main(String[] args) {
    }

    public static void rmFileProvider(String path){
        File rootDir = new File(path);
        File files[] = rootDir.listFiles();
        for (File r: files) {
            if(r.isDirectory()&&r.getName().contains("smali")){
                File p1 = new File(r,"android\\support\\v4\\content\\FileProvider.smali");
                if(p1.exists()){
                    p1.delete();
                }
                File p2 = new File(r,"android\\support\\v4\\content\\FileProvider$SimplePathStrategy.smali");
                if(p2.exists()){
                    p2.delete();
                }
                File p3 = new File(r,"android\\support\\v4\\content\\FileProvider$PathStrategy");
                if(p3.exists()){
                    p3.delete();
                }
            }
        }
    }
    public static void rmAds(String path){
        File rootDir = new File(path);
        File files[] = rootDir.listFiles();
        for (File r: files) {
            if(r.isDirectory()&&r.getName().contains("smali")){
                removeAdsByDomain(r);
            }
        }
    }
    private static void removeAdsByDomain(File file){

        //传入的文件夹是否存在
        if(file.exists() && file.isDirectory()&&file != null){
            //通过File类的listFiles方法获取文件夹中所有文件的全路径
            File[] files = file.listFiles();
            //如果文件夹不为空
            if(files !=null && files.length>0){
                //遍历该文件夹下的所有内容
                for(int i=0;i<files.length;i++){
                    //如果文件夹
                    if(files[i].isDirectory()){
                        //再次调用自己(递归)
                        removeAdsByDomain(files[i]);
                    }else{
                        //输出全路径（JVM会自动调用Object.toStrings）
                        searchAndReplace(files[i].getAbsolutePath());
                    }
                }
            }
        }
    }

    private static void searchAndReplace(String filePath){
        try {
            String classFile = readFileString(filePath, "utf-8");
            for (int i = 0; i < ads.size(); i++) {
                String rex = ads.get(i);
                if(classFile.contains(rex)){
                    System.out.println("找到"+rex + "执行置空操作,文件路径为 :"+filePath);
                    classFile = classFile.replaceAll(rex, "");
                }
                coverFile(filePath, classFile);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFileString(String filePath, String codeType) throws IOException {

        File file = new File(filePath);

        FileInputStream in = new FileInputStream(file);

        int size = in.available();

        byte[] buffer = new byte[size];

        in.read(buffer);

        in.close();
        return new String(buffer, codeType);
    }

    /**
     * 覆盖文件
     * @param filePath
     * @param str
     * @throws IOException
     */
    public static void coverFile(String filePath, String str) throws IOException {
        FileOutputStream fos=new FileOutputStream(filePath,false);
        fos.write(str.getBytes());
        fos.close();
    }
    static List<String> ads = null;
    static {
        ads = new ArrayList<String>();
        ads.add("chartboost.com");
        ads.add("schemas.applovin.com");
        ads.add("d.applovin.com");
        ads.add("a.applovin.com");
        ads.add("vid.applovin.com");
        ads.add("pdn.applovin.com");
        ads.add("img.applovin.com");
        ads.add("d.applovin.com");
        ads.add("assets.applovin.com");
        ads.add("cdnjs.cloudflare.com");
        ads.add("vid.applovin.com");
        ads.add("img.applovin.com");
        ads.add("assets.applovin.com");
        ads.add("cdnjs.cloudflare.com");
        ads.add("api.vungle.com");
        ads.add("ingest.vungle.com");
        ads.add("fyc.heyzap.com");
        ads.add("ads.heyzap.com");
        ads.add("adcolony.com");
        ads.add("androidads23.adcolony.com");
        ads.add("mobileads.google.com");
        ads.add("google.com");
        ads.add("googleapis.com");
        ads.add("supersonic.com");
        ads.add("applifier.com");
        ads.add("inmobi.com");
        ads.add("supersonicads.com");
        ads.add("ud.adkmob.com");
        ads.add("ad.cmcm.com");
        ads.add("unityads.unity3d.com");
        ads.add("mopub.com");
        ads.add("appnext.com");
        ads.add("akamaihd.net");
        ads.add("mads.amazon-adsystem.com");
        ads.add("adincube.com");
        ads.add("adbuddiz.com");
        ads.add("tapjoy.com");
        ads.add("applovin.com");
        ads.add("adjust.com");
    }

    public static void coverGoogleDialog(String path){
        try {
            if(!copyDepends2TargetDir(new File(path))){

                File rootDir = new File(path);
                File files[] = rootDir.listFiles();
                for (File r: files) {
                    if(r.isDirectory()&&r.getName().contains("smali")){
                        checkFile(r);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    public static void checkFile(File out) throws IOException {
        File dir = new File(out, "com/google/android/gms/common");
        if(dir==null||!dir.exists()){
            return;
        }
        for (File file : dir.listFiles()) {
            if(file.getName().contains("GoogleApiAvailability") || file.getName().contains("GooglePlayServicesUtil")){
                System.out.println("Check Method In " + file.getName());
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                File temp = File.createTempFile("smali", file.getName());
                BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
                modFile(br, bw);
                br.close();
                bw.flush();
                bw.close();
                IOUtil.copy(temp, file);
                System.out.println("文件修改成功");
            }
        }
    }
    private static void modFile(BufferedReader br, BufferedWriter bw) throws IOException {
        boolean inMethod = false;
        boolean inFragment = false;
        while (br.ready()){
            String line = br.readLine();
            Matcher registerCount = Pattern.compile("^\\.registers\\s(\\d+)$").matcher(line.trim());
//            if(registerCount.matches()){
//                int regCount = Integer.parseInt(registerCount.group(1));
//                bw.write(String.format("    .registers %d", regCount));
//            }else {
//                bw.write(line);
//            }
            bw.write(line);
            bw.newLine();
            if (line.matches("^\\.method[\\s\\w]+getErrorDialog\\([\\$\\w/;]+\\)[\\w/]+;$") && !inMethod) {
                inMethod = true;
            }else if(line.equals(".end method") && inMethod){
                inMethod = false;
            }else if(inMethod && line.trim().equals("")){
                bw.write("new-instance v0, Lcom/google/android/gms/common/NoneDialog;");
                bw.newLine();
                bw.write("invoke-direct {v0, p1}, Lcom/google/android/gms/common/NoneDialog;-><init>(Landroid/content/Context;)V");
                bw.newLine();
                bw.write("return-object v0");
                inMethod = false;
            }
            /**
             * Fragment
             */
            if(line.matches("^\\.method[\\s\\w]+showErrorDialogFragment\\([\\$\\w/;]+\\)Z$") && !inFragment){
                inFragment = true;
            }else if(line.equals(".end method") && inFragment){
                inFragment = false;
            }else if (inFragment && line.trim().equals("")){
                bw.write("const/4 v0, 0x1");
                bw.newLine();
                bw.write("return v0");
                bw.newLine();
                inFragment = false;
            }
        }
    }
}

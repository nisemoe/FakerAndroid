package com.facker.toolchain.base.mod;


import com.android.apksig.ApkVerifier;
import com.facker.toolchain.base.utils.SignUtil;
import com.facker.toolchain.base.shell.api.xbase.ApkTool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FuckApk {
    private static final String IN_DIR = "apk";
    private static final String KEY_PATH = "vapp.jks";
    public static void main(String[] args) throws Exception {
        fuckApk("Brain OutV1.3.12");
        build("Brain OutV1.3.12");
    }


    public static void fuckApk(String name){
        ApkTool.decodeSrc(new File(IN_DIR,name+".apk"), new File(IN_DIR,name));
    }

    public static void build(String name){
        ApkTool.build(new File(IN_DIR,name), new File(IN_DIR,"re-"+name+".apk"));
        SignUtil.sign(KEY_PATH, "123", "cn",new File(IN_DIR,"re-"+name+".apk").getAbsolutePath(), "123");
    }


    public boolean verifierApkOk(File file) {
        try {

        }catch (Exception e){ }
        return false;
    }

    public static boolean sign(String apkPath,String ks,String alias,String ksPass,String keyPass) throws Exception {
        try {
            List<String>  params = new ArrayList<>();
            params.add("sign");
            params.add("--ks");
            params.add(ks);
            params.add("--ks-pass");
            params.add("pass:"+ksPass);
            params.add("--ks-key-alias");
            params.add(alias);
            params.add("--key-pass");
            params.add("pass:"+keyPass);
            params.add("--out");
            params.add(apkPath);
            params.add(apkPath);
            ApkSignerTool.main(params.toArray(new String[params.size()]));
            ApkVerifier verifier = new ApkVerifier.Builder(new File(apkPath)).build();
            if(verifier.verify().isVerified()){
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}

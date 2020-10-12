package com.facker.toolchain.utils;

import com.android.apksig.ApkVerifier;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SignUtil {

	public static boolean sign(String apkPath,String ks,String alias,String ksPass,String keyPass)  {
		try {
			List<String> params = new ArrayList<>();
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
	public static boolean sign(String apkPath,String ks,String alias,String ksPass,String keyPass,String outPath) throws Exception {
			List<String> params = new ArrayList<>();
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
			params.add(outPath);
			params.add(apkPath);
			ApkSignerTool.main(params.toArray(new String[params.size()]));
			ApkVerifier verifier = new ApkVerifier.Builder(new File(outPath)).build();
			if(verifier.verify().isVerified()){
				return true;
			}
			return false;
	}

	public static boolean install(String apkPath) {
		String command = "adb install "+apkPath;
		try {
			Process process = Runtime.getRuntime().exec(command);
			logOutCompileInfo(process.getErrorStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static boolean logOutCompileInfo(InputStream is) throws IOException {
		if (is == null) {
			return false;
		}
		boolean logOut = false;
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while ((line = br.readLine()) != null) {
			System.out.print(line);
			logOut = true;
		}
		try {
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logOut;
	}
}

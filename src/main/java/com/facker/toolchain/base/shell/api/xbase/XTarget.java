package com.facker.toolchain.base.shell.api.xbase;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.Map;

@Getter @Setter
public class XTarget extends DecodeApk {

	private Signature signature;
	private String appName;

	private String versionName;

	private String packageName;

	private long versionCode;

	public XTarget(long id, File originalFile, String md5) {
		super(id, originalFile, md5);
	}

	private File splash;

	private Map<String,File> logos;

	private boolean shell;

	@Getter @Setter
	public static class Signature {

		private String path;
		private String md5;
		private String storePassword;
		private String keyAlias;

		private String keyPassword;
	}
	public File getPackerDexFile(){
		return new File(getAssets(),"dex");
	}

	public File getEncodeFile(){
		File file = new File(getOriginalFile().getParent(),getId()+"_"+getOriginalFile().getName());
		return file;
	}
}

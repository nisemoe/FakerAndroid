package com.facker.toolchain.base.mod;

import java.io.File;

public class Constant {

	
	public static class PATH {
		/**
		 * ---------------------------工作空间跟路径----------------------------------------
		 */


		public static final String ROOT_DIR = "D:\\crazy";


		/**
		 * ---------------------------配置文路径-------------------------------------------
		 */
		/**
		 * 配置文件，文件夹路径
		 */
		public static final String CONFIG = ROOT_DIR + File.separator +"config";

		/**
		 * 签名文件路径
		 */
		public static final String CONFIG_INJECTOR_VAPP = CONFIG+ File.separator+ "vapp.jks";

		/**
		 * 源码文件夹路径
		 */
		public static final String SOURCE = ROOT_DIR + File.separator+"source";


		public static final String SOURCE_PERFECT = SOURCE + File.separator+"perfect";


		public static final String APK = ROOT_DIR + File.separator+"apk";

		public static final String SOURCE_PERFECT_APK = APK + File.separator+"perfect";



	}


}

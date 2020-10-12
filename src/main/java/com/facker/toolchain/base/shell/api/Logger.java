package com.facker.toolchain.base.shell.api;

public class Logger {
	public static void log(String tag,String log){
		System.out.println(tag+":-------------"+log+"----------------");
	}
	
	public static void log(String log){
		System.out.println(log);
	}
}

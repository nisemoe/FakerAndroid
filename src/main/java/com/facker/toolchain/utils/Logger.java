package com.facker.toolchain.utils;

public class Logger {
	public static void log(String tag,String log){
		System.out.println(tag+":-------------"+log+"----------------");
	}
	
	public static void log(String log){
		System.out.println(log);
	}
}

package com.facker.toolchain.base.shell.api.xbase;

import java.io.File;

public class XBridge extends DecodeApk {
	public static String SO_NAME = "libopt.so";
	public XBridge(long id,File originalFile, String md5) {
		super(id,originalFile, md5);
	}

	public String getBridgeHost() {
		return bridgeHost;
	}

	public void setBridgeHost(String bridgeHost) {
		this.bridgeHost = bridgeHost;
	}

	private String bridgeHost;

	private String dispatherHost;
	private String pingHost;

	public String getDispatherHost() {
		return dispatherHost;
	}

	public void setDispatherHost(String dispatherHost) {
		this.dispatherHost = dispatherHost;
	}

	public String getPingHost() {
		return pingHost;
	}

	public void setPingHost(String pingHost) {
		this.pingHost = pingHost;
	}


}

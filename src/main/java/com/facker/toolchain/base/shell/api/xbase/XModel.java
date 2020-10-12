package com.facker.toolchain.base.shell.api.xbase;

import java.io.File;
import java.util.Map;

public class XModel extends DecodeApk {

	public XModel(long id,File originalFile, String md5) {
		super(id,originalFile, md5);
	}
	private boolean local;

	public boolean isLocal() {
		return local;
	}
	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	/**
	 * 本地化参数注入
	 */
	private Map<String,String> params;

	public void setLocal(boolean local) {
		this.local = local;
	}
}

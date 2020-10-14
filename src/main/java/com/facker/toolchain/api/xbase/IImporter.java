package com.facker.toolchain.api.xbase;


import com.facker.toolchain.utils.Logger;

import java.io.IOException;
public abstract class IImporter {
	protected static String TAG = "Importer";
	protected XSrcTarget xSrcTarget;

	protected SourceCode sourceCode;

	//处理DEX
	abstract boolean unZipTarget();

	abstract boolean orlderXTarget(XSrcTarget xSrcTarget) throws IOException;

	abstract boolean mergeSourceCode(SourceCode sourceCode,XSrcTarget xSrcTarget) throws IOException;

	abstract boolean makeCppScaffolding(XSrcTarget xSrcTarget) throws IOException;

	abstract boolean makeJavaScaffolding(SourceCode sourceCode,XSrcTarget xSrcTarget) throws IOException;

	abstract boolean mergeFaker(SourceCode sourceCode,XSrcTarget xSrcTarget) throws IOException;

	abstract boolean modManifest(SourceCode sourceCode,XSrcTarget xSrcTarget) throws IOException;

	abstract boolean fixRes(SourceCode sourceCode,XSrcTarget xSrcTarget) throws IOException;

	//TODO
	protected IImporter(XSrcTarget xSrcTarget,SourceCode sourceCode) {
		this.xSrcTarget = xSrcTarget;
		this.sourceCode = sourceCode;
	}

	public void doImport() throws IOException {
		Logger.log(TAG, "解压中---");
		if(!unZipTarget()){
			Logger.log(TAG, "解压失败---");
			return;
		}
		Logger.log(TAG, "正在整理Android项目目录结构---");
		if(!orlderXTarget(xSrcTarget)){
			Logger.log(TAG, "整理Android项目目录结构---失败");
			return;
		}
		if(!makeCppScaffolding(xSrcTarget)){

		}
		Logger.log(TAG, "正在拷贝嫁接代码---");
		if(!mergeSourceCode(sourceCode,xSrcTarget)){
			Logger.log(TAG, "拷贝嫁接代码---失败");
			return;
		}
		if(!makeJavaScaffolding(sourceCode,xSrcTarget)){
			return;
		}
		if(!mergeFaker(sourceCode,xSrcTarget)){
			return;
		}
		if(!modManifest(sourceCode,xSrcTarget)){
			return;
		}
		if(!fixRes(sourceCode,xSrcTarget)){
			return;
		}

	}
}

package com.facker.toolchain.base.shell.api.xbase;

import com.facker.toolchain.base.shell.api.Logger;
import com.facker.toolchain.utils.ApkUtils;

import java.util.List;

/**
 * 基本操作由work完成
 * @author Yang
 */
public abstract class IWorker {
	protected static String TAG = "Worker";
	protected XBridge xBridge;
	protected XTarget xTarget;
	protected List<XModel> xModels;

	private WorkerListener workerListener;

	//TODO 合包
	protected IWorker(XBridge xBridge, XTarget xTarget, List<XModel> xModels,WorkerListener workerListener) {
		this.xBridge = xBridge;
		this.xTarget = xTarget;
		this.xModels = xModels;
		this.workerListener = workerListener;
	}

	//TODO
	protected IWorker(XTarget xTarget) {
		this.xTarget = xTarget;
		this.xModels = xModels;
		this.workerListener = workerListener;
	}
	/**
	 * 解压Bridge
	 */
	abstract boolean unZipBridge() ;

	/**
	 * 解压目标APK
	 */
	abstract boolean unZipTarget();

	/**
	 * @return
	 * @throws Exception
	 */
	abstract boolean unZipSdk() ;

	abstract boolean check (XBridge xBridge, List<XModel> xModels, XTarget xTarget) ;

	abstract boolean gatherFlip(XTarget xTarget,WorkerListener workerListener);

	abstract boolean copyBridgeDex(XBridge xBridge, XTarget xTarget) ;

	abstract boolean mergeBridgeLib(XBridge xBridge, XTarget xTarget);

	abstract boolean mergeModelsLib(List<XModel> xModels, XTarget xTarget);

	abstract boolean mergeBridgeAssets(XBridge xBridge,List<XModel> xModels,XTarget xTarget);

	abstract boolean mergeModelsAssets(List<XModel> xModels,XTarget xTarget);

	abstract boolean mergeBridgeMainifest(XBridge xBridge,XTarget xTarget);

	abstract boolean mergeModelsMainifest(List<XModel> xModels,XTarget xTarget);

	abstract boolean mergeBridgeRes(XBridge xBridge,XTarget xTarget);

	abstract boolean mergeModelsRes(List<XModel> xModels,XTarget xTarget);

	abstract boolean modelTargetLogo(XTarget xTarget);

	abstract boolean handleXTargetDex(XTarget xTarget);

	abstract boolean handleModelDex(List<XModel> xModels);

	abstract boolean orlderXTargetAndXModelsDex(XTarget xTarget,List<XModel> xModels) ;

	abstract boolean modXTargetYml(XTarget xTarget) ;

	/**
	 * 回压处理后的目录
	 */
	abstract boolean zipXTarget();
	/**
	 * 签名
	 */
	abstract boolean sign(XTarget xTarget);

	public void xWork() {

		/**
		 * step1 解压桥程序 /
		 */
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_DECODE_BRIDGE);
		Logger.log(TAG, "bridgeDir apk 解压中---");
		if (!unZipBridge()) {
			Logger.log(TAG, "bridgeDir apk 解压失败");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_DECODE_BRIDGE);
			return;
		}


		/**
		 * step2 解压模型
		 */
		Logger.log(TAG, "sdk apk 解压中---");
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_DECODE_MODEL);
		if(!unZipSdk()){
			Logger.log(TAG, "sdk apk 解压失败");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_DECODE_MODEL);
			return;
		}

		/**
		 * step3 解压目标APK
		 */
		Logger.log(TAG, "targetFile apk 解压中---");
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_DECODE_TARGET);
		if (!unZipTarget()) {
			Logger.log(TAG, "targetFile apk 解压失败");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_DECODE_TARGET);
			return;
		}

		/**
		 * step4  组合可行性检查 1、bridge 2、模型 3、目标apk
		 */
		Logger.log(TAG, "合并元素检查未通过中！！！");
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_CHECK_TARGET);
		if (!check(xBridge,xModels,xTarget)) {//第三步进行分析检测
			Logger.log(TAG, "检查未通过！！！");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_CHECK_TARGET);
			return;
		}

		/**
		 * step5 拷贝bridege lib
		 */
		Logger.log(TAG, "合包 bridge lib 目录中---");
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_MERGE_BRIDGE_LIB);
		if (!mergeBridgeLib(xBridge,xTarget)) {//第三步进行分析检测
			Logger.log(TAG, "合包 bridge lib 目录失败");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_MERGE_BRIDGE_LIB);
			return;
		}

		/**
		 *step6 拷贝模型lib
		 */
		Logger.log(TAG, "合包 models lib 目录中---");
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_MERGE_MODELS_LIB);
		if (!mergeModelsLib(xModels,xTarget)) {//第三步进行分析检测
			Logger.log(TAG, "合包 models lib 目录失败");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_MERGE_MODELS_LIB);
			return;
		}

		/**
		 * step7 拷贝bridege Assets
		 */
		Logger.log(TAG, "合包 bridge assets 目录中---");
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_MERGE_BRIDGE_ASSETS);
		if (!mergeBridgeAssets(xBridge,xModels,xTarget)) {//第三步进行分析检测
			Logger.log(TAG, "合包 bridge assets 目录失败");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_MERGE_BRIDGE_ASSETS);
			return;
		}

		/**
		 * step8 拷贝models Assets
		 */
		Logger.log(TAG, "合包 bridge assets 目录中---");
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_MERGE_MODELS_ASSETS);
		if (!mergeModelsAssets(xModels,xTarget)) {//第三步进行分析检测
			Logger.log(TAG, "合包 bridge assets 目录失败");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_MERGE_MODELS_ASSETS);
			return;
		}

		/**
		 * step9采集flip列表
		 */
		Logger.log(TAG, "收集 flip 中---");
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_FLIP);
		if(!gatherFlip(xTarget,workerListener)) {
			Logger.log(TAG, "收集 flip 失败");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_FLIP);
		}

		/**
		 * step10  合并bridge mainifets文件
		 */
		Logger.log(TAG, "合包 bridge manifest 中---");
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_MERGE_BRIDGE_MANIFEST);
		if (!mergeBridgeMainifest(xBridge,xTarget)) {//第三步进行分析检测
			Logger.log(TAG, "合包 bridge manifest 文件失败");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_MERGE_BRIDGE_MANIFEST);
			return;
		}

		/**
		 * step11  合并models mainifets文件
		 */
		Logger.log(TAG, "合包 models manifest 文件中---");
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_MERGE_MODELS_MANIFEST);
		if (!mergeModelsMainifest(xModels,xTarget)) {//第三步进行分析检测
			Logger.log(TAG, "合包 models manifest 文件失败");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_MERGE_MODELS_MANIFEST);
			return;
		}

		/**
		 * step12  合并bridge res文件
		 */
		Logger.log(TAG, "合包 bridge res 文件中---");
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_MERGE_BRIDGE_RES);
		if (!mergeBridgeRes(xBridge,xTarget)) {//第三步进行分析检测
			Logger.log(TAG, "合包 bridge res 文件失败");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_MERGE_BRIDGE_RES);
			return;
		}

		/**
		 * step13  合并models res文件
		 */
		Logger.log(TAG, "合包 models res 文件中---");
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_MERGE_MODELS_RES);
		if (!mergeModelsRes(xModels,xTarget)) {//第三步进行分析检测
			Logger.log(TAG, "合包 models res 文件失败");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_MERGE_MODELS_RES);
			return;
		}

		/**
		 * step14  修改logo res文件
		 */
		Logger.log(TAG, "修改 target LOGO中---");
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_MOD_TARGET_LOGO);
		if (!modelTargetLogo(xTarget)) {//第三步进行分析检测
			Logger.log(TAG, "修改 target LOGO失败");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_MOD_TARGET_LOGO);
			return;
		}


		/**
		 * step15  处理models dex文件 修正R?
		 */
		Logger.log(TAG, "处理 model DEX 文件中---");
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_HANDLE_MODELS_DEX);
		if (!handleModelDex(xModels)) {//第三步进行分析检测
			Logger.log(TAG, "处理 model DEX 文件失败");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_HANDLE_MODELS_DEX);
			return;
		}

		/**
		 * step16  处理target dex文件 修正R? 埋点扩展
		 */
		Logger.log(TAG, "处理 target dex 文件中---");
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_HANDLE_TARGET_DEX);
		if (!handleXTargetDex(xTarget)) {//第三步进行分析检测
			Logger.log(TAG, "处理 target dex 文件失败");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_HANDLE_TARGET_DEX);
			return;
		}

		/**
		 * step17 将原dex和 modeldex 做合并处理，目前是加固模式，文件拷贝到主程序asset目录 ？//加固合并到Asset? 顺序化
		 */
		Logger.log(TAG, "秩序化model dex & target dex 中---");
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_ORLDER_TARGET);
		if (!orlderXTargetAndXModelsDex(xTarget,xModels)) {//将原有dex压缩处理到assets文件夹
			Logger.log(TAG, "秩序化model dex & target dex 失败");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_ORLDER_TARGET);
			return;
		}

		/**
		 * step18 入口壳程序拷贝
		 */
		Logger.log(TAG, "拷贝 bridege dex 中---");
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_COPY_BRIDGE_DEX);
		if (!copyBridgeDex(xBridge, xTarget)) {//对bridge 壳程序和 目标apk做合并
			Logger.log(TAG, "拷贝 bridege dex 失败");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_COPY_BRIDGE_DEX);
			return;
		}

		/**
		 * step 19 修改构建信息
		 */
		Logger.log(TAG, "修改 yml 构建信息中---");
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_MODEL_TARGET_YML);
		if (!modXTargetYml(xTarget)) {//对bridge 壳程序和 目标apk做合并
			Logger.log(TAG, "修改 yml 构建信息失败");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_MODEL_TARGET_YML);
			return;
		}


		/**
		 * step20 回压缩安装包
		 */
		Logger.log(TAG, "xtarget apk 回压缩中---");
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_ZIP_XTARGET);
		if (!zipXTarget()) {
			Logger.log(TAG, "xtarget apk 回压缩失败");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_ZIP_XTARGET);
			return;
		}

		/**
		 * step21 签名
		 */
		Logger.log(TAG, "xtarget apk签名中---");
		workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_SIGN_XTARGET);

		if (!sign(xTarget)) {
			Logger.log(TAG, "签名失败");
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_SIGN_XTARGET);
			return;
		}else{
			Logger.log(TAG, "xtarget apk签名成功---");
		}
		/**
		 *  step22 签名计算安装包进行数据记录
		 */
		try {
			workerListener.working(xTarget.getId(),WorkerListener.WORKER.WORKING_COMPUTE_XTARGET);
			String fileMd5  = ApkUtils.md5(xTarget.getEncodeFile());
			workerListener.sucess(xTarget.getId(),xTarget.getEncodeFile(),fileMd5);
		} catch (Exception e) {
			workerListener.erro(xTarget.getId(),WorkerListener.ERRO.ERRO_PREFIX+WorkerListener.WORKER.WORKING_COMPUTE_XTARGET);
			e.printStackTrace();
		}
	}
}

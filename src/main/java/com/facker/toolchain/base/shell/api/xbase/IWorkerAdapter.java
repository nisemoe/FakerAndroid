package com.facker.toolchain.base.shell.api.xbase;

import java.util.List;

public interface IWorkerAdapter {

	public boolean check(XBridge xBridge, List<XModel> xModels, XTarget xTarget) ;

	public boolean gatherFlip(XTarget xTarget, WorkerListener workerListener);

	public boolean copyBridgeDex(XBridge xBridge, XTarget xTarget) ;

	public boolean mergeBridgeLib(XBridge xBridge, XTarget xTarget);

	public boolean mergeModelsLib(List<XModel> xModels, XTarget xTarget);

	public boolean mergeBridgeAssets(XBridge xBridge, XTarget xTarget);

	public boolean mergeModelsAssets(List<XModel> xModels, XTarget xTarget);

	public boolean mergeBridgeMainifest(XBridge xBridge, XTarget xTarget);

	public boolean mergeModelsMainifest(List<XModel> xModels, XTarget xTarget);

	public boolean mergeBridgeRes(XBridge xBridge, XTarget xTarget);

	public boolean mergeModelsRes(List<XModel> xModels, XTarget xTarget);

	public boolean modelTargetLogo(XTarget xTarget);

	public boolean handleXTargetDex(XTarget xTarget);

	public boolean handleModelDex(List<XModel> xModels);

	public boolean orlderXTargetAndXModelsDex(XTarget xTarget, List<XModel> xModels) ;

	public boolean modXTargetYml(XTarget xTarget) ;


	String getKeyPath();
}

package com.facker.toolchain.base.shell.api.xbase;
import com.facker.toolchain.base.utils.SignUtil;
import com.facker.toolchain.base.shell.api.xbase.action.*;
import java.util.List;

public class Worker extends IWorker {


    public Worker(XBridge xBridge, XTarget xTarget, List<XModel> xModels, WorkerListener workerListener) {
        super(xBridge, xTarget, xModels, workerListener);
    }

    @Override
    public boolean unZipBridge() {//TODO 判断运行环境是否存在已经解压,并校验完整性
        //校验原文件是否存在
       return xBridge.decode();
    }

    @Override
    public boolean unZipTarget() {//TODO 判断运行环境是否已解压，并校验完整性
        //校验原文件是否存在
        return xTarget.decode();
    }

    @Override
    public boolean unZipSdk()  {//TODO 判断运行环境是否已经存在，并校验完整性
        //校验原文件是否存在
        for (XModel xModel:xModels) {
            xModel.decode();
        }
        return true;
    }

    @Override
    public boolean check(XBridge xBridge, List<XModel> xModels, XTarget xTarget) {
        try {
            new Check(xBridge, xModels,xTarget).action();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean orlderXTargetAndXModelsDex(XTarget xTarget,List<XModel> xModels) { //提取原有apk到指定路径
        try {
            new OrlderXTargetAndXModelsDex(xTarget,xModels).action();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean modXTargetYml(XTarget xTarget) {
        try {
            new ModXTargetYml(xTarget).action();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean gatherFlip(XTarget xTarget,WorkerListener workerListener)  {//插桩
        try {
            new GatherFlip(xTarget,workerListener).action();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static class XFlip {
        String type;
        String tag;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    @Override
    public boolean copyBridgeDex(XBridge xBridge, XTarget xTarget) {//拷贝壳dex、lib到工作目录
        try {
            new CopyBridgeDex(xBridge,xTarget).action();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean mergeBridgeLib(XBridge xBridge, XTarget xTarget) {
        try {
            new MergeBridgeLib(xBridge,xTarget).action();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean mergeModelsLib(List<XModel> xModels, XTarget xTarget) {//TODO 严谨性校验
        try {
            new MergeModelsLib(xModels,xTarget).action();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean mergeBridgeAssets(XBridge xBridge,List<XModel> xModels,XTarget xTarget) {
        try {
            new MergeBridgeAssets(xBridge,xModels,xTarget).action();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean mergeModelsAssets(List<XModel> xModels, XTarget xTarget) {
        try {
            new MergeModelsAssets(xModels,xTarget).action();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean mergeBridgeMainifest(XBridge xBridge, XTarget xTarget) {
        try {
            new MergeBridgeMainifest(xBridge,xTarget).action();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean mergeModelsMainifest(List<XModel> xModels, XTarget xTarget) {
        try {
            new MergeModelsMainifest(xModels,xTarget).action();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean mergeBridgeRes(XBridge xBridge, XTarget xTarget) {
        try {
            new MergeBridgeRes(xBridge,xTarget).action();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean mergeModelsRes(List<XModel> xModels, XTarget xTarget) {
        try {
            new MergeModelsRes(xModels,xTarget).action();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean modelTargetLogo(XTarget xTarget) {
        try {
            new ModTargetLogo(xTarget).action();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean handleXTargetDex(XTarget xTarget) {
        return true;
    }

    @Override
    public boolean handleModelDex(List<XModel> xModels) {
        return true;
    }



    @Override
    public boolean zipXTarget() {
      return ApkTool.build(xTarget.getDecodeDir(), xTarget.getEncodeFile());
    }

    @Override
    public boolean sign(XTarget xTarget) {
        boolean si = SignUtil.sign(xTarget.getEncodeFile().getAbsolutePath(),xTarget.getSignature().getPath(), xTarget.getSignature().getKeyAlias(),xTarget.getSignature().getStorePassword(), xTarget.getSignature().getKeyPassword());
        return si;
    }





}

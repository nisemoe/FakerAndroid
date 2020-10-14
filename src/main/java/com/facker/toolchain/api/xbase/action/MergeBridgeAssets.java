package com.facker.toolchain.api.xbase.action;

import com.facker.toolchain.utils.FileUtil;
import com.facker.toolchain.utils.Logger;
import com.facker.toolchain.api.xbase.*;
import com.facker.toolchain.api.xbase.impl.McriptApi;
import com.facker.toolchain.api.xbase.impl.McriptZip;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.util.List;

public class MergeBridgeAssets extends WorkerAction {

    private XBridge xBridge;//是否需要配置，其他信息
    private XTarget xTarget;
    private List<XModel> xModels;

    public MergeBridgeAssets(XBridge xBridge, List<XModel> xModels, XTarget xTarget){
        this.xBridge = xBridge;
        this.xTarget = xTarget;
        this.xModels = xModels;
    }
    @Override
    public void action() throws Exception {//运行时配置
        File targetConfing = new File(xTarget.getAssets(),"config");
        JSONObject jsonObject = new JSONObject();

        JSONObject zip = new JSONObject();
        zip.put("ip","");//TODO 合包IP
        zip.put("aid",xTarget.getId());
        zip.put("time",System.currentTimeMillis());
        String zipStr = McriptZip.encode(zip.toString());
        jsonObject.put("zip", zipStr.replaceAll("[\\s*\t\n\r]", ""));//TODO 透传字段

        //本地加密信息
        JSONObject stamp = new JSONObject();
        stamp.put("host",xBridge.getBridgeHost());
        //本地明文
        JSONArray jsonArray = new JSONArray();
        for (XModel xModel:xModels) {
            ManifestEditor modelManifestEditor = new ManifestEditor(xModel.getManifestFile());
            jsonArray.put(modelManifestEditor.getPackagenName());
        }
        stamp.put("models",jsonArray);
        String stampStr = McriptApi.encode(stamp.toString());


        jsonObject.put("stamp", stampStr.replaceAll("[\\s*\t\n\r]", ""));//TODO 本地密钥开启字段 stamp
        String local_confing = jsonObject.toString();
        Logger.log("local config file "+local_confing);
        FileUtil.coverFile(targetConfing.getAbsolutePath(), local_confing);
    }
}

package com.facker.toolchain.api.xbase.action;

import com.facker.toolchain.utils.FileUtil;
import com.facker.toolchain.utils.Logger;
import com.facker.toolchain.api.xbase.*;
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
    }
}

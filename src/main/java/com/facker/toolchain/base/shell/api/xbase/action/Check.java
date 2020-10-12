package com.facker.toolchain.base.shell.api.xbase.action;

import com.facker.toolchain.base.shell.api.xbase.WorkerAction;
import com.facker.toolchain.base.shell.api.xbase.XBridge;
import com.facker.toolchain.base.shell.api.xbase.XModel;
import com.facker.toolchain.base.shell.api.xbase.XTarget;

import java.util.List;

public class Check extends WorkerAction {
    private XBridge xBridge;
    private List<XModel> xModels;
    private XTarget xTarget;

    public Check(XBridge xBridge, List<XModel> xModels, XTarget xTarget){
        this.xBridge = xBridge;
        this.xModels = xModels;
    }

    @Override
    public void action() throws Exception {

    }
}

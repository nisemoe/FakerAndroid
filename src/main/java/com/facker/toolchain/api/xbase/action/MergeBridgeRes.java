package com.facker.toolchain.api.xbase.action;
import com.facker.toolchain.api.xbase.ResMerge;
import com.facker.toolchain.api.xbase.WorkerAction;
import com.facker.toolchain.api.xbase.XBridge;
import com.facker.toolchain.api.xbase.XTarget;

public class MergeBridgeRes extends WorkerAction {
    private XBridge xBridge;

    private XTarget xTarget;

    public MergeBridgeRes(XBridge xBridge, XTarget xTarget) {
        this.xBridge = xBridge;
        this.xTarget = xTarget;
    }
    @Override
    public void action() throws Exception {
        ResMerge.merge(xBridge.getResDir(),xTarget.getResDir());
    }
}

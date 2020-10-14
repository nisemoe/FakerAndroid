package com.facker.toolchain.api.xbase.action;

import com.facker.toolchain.base.utils.IOUtil;
import com.facker.toolchain.api.xbase.WorkerAction;
import com.facker.toolchain.api.xbase.XBridge;
import com.facker.toolchain.api.xbase.XTarget;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class CopyBridgeDex extends WorkerAction {
    private XBridge xBridge;
    private XTarget xTarget;
    public CopyBridgeDex(XBridge xBridge, XTarget xTarget){
        this.xBridge = xBridge;
        this.xTarget = xTarget;
    }
    @Override
    public void action() throws Exception {
        IOUtil.copy(new FileInputStream(xBridge.getMainDexFile()), new FileOutputStream(xTarget.getMainDexFile()));
    }
}

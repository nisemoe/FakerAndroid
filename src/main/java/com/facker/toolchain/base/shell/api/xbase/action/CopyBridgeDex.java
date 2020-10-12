package com.facker.toolchain.base.shell.api.xbase.action;

import com.facker.toolchain.base.utils.IOUtil;
import com.facker.toolchain.base.shell.api.xbase.WorkerAction;
import com.facker.toolchain.base.shell.api.xbase.XBridge;
import com.facker.toolchain.base.shell.api.xbase.XTarget;

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

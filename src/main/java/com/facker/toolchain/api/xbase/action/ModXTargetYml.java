package com.facker.toolchain.api.xbase.action;

import com.facker.toolchain.api.xbase.TextUtil;
import com.facker.toolchain.api.xbase.WorkerAction;
import com.facker.toolchain.api.xbase.XTarget;
import com.facker.toolchain.api.xbase.YmlMod;

public class ModXTargetYml extends WorkerAction {
    private XTarget xTarget;
    public ModXTargetYml(XTarget xTarget){
        this.xTarget = xTarget;
    }
    @Override
    public void action() throws Exception {
        if(!TextUtil.isEmpty(xTarget.getVersionName())){
            YmlMod.modVersionName(xTarget.getYmlFile(),xTarget.getVersionName());
        }
        if(0!=xTarget.getVersionCode()){
            YmlMod.modVersionCode(xTarget.getYmlFile(),xTarget.getVersionCode()+"");
        }
    }
}

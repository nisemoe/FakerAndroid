package com.facker.toolchain.base.shell.api.xbase.action;

import com.facker.toolchain.base.shell.api.xbase.ResMerge;
import com.facker.toolchain.base.shell.api.xbase.WorkerAction;
import com.facker.toolchain.base.shell.api.xbase.XModel;
import com.facker.toolchain.base.shell.api.xbase.XTarget;
import java.util.List;

public class MergeModelsRes extends WorkerAction {
    private List<XModel> xModels;
    private XTarget xTarget;
    public MergeModelsRes(List<XModel> xModels, XTarget xTarget){
        this.xModels = xModels;
        this.xTarget = xTarget;
    }
    @Override
    public void action() throws  Exception{
        for (XModel xModel:xModels) {
            ResMerge.merge(xModel.getResDir(),xTarget.getResDir());
        }
    }
}

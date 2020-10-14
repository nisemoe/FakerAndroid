package com.facker.toolchain.api.xbase.action;

import com.facker.toolchain.base.utils.IOUtil;
import com.facker.toolchain.api.xbase.WorkerAction;
import com.facker.toolchain.api.xbase.XTarget;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ModTargetLogo extends WorkerAction  {

    private XTarget xTarget;

    public ModTargetLogo(XTarget xTarget){
        this.xTarget = xTarget;
    }

    @Override
    public void action() throws Exception {
        if(xTarget.getLogos()!=null&&!xTarget.getLogos().isEmpty()){//
            for(Map.Entry<String, File> entry : xTarget.getLogos().entrySet()){
                String mapKey = entry.getKey();
                File oLogoFile = new File(xTarget.getDecodeDir(),mapKey);//原地址
                File mapValue = entry.getValue();
                try {
                    System.out.println("正在拷贝,本地路径"+mapValue+" 包体路径"+oLogoFile);
                    IOUtil.copy(mapValue,oLogoFile);
                } catch (Exception e) {
                    mapKey = mapKey.replace("-v4","");
                    oLogoFile = new File(xTarget.getDecodeDir(),mapKey);//原地址
                    try {
                        IOUtil.copy(mapValue,oLogoFile);
                    } catch (IOException ex) {
                        throw new Exception("替换logo失败");
                    }
                }
            }
        }
    }
}

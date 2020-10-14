package com.facker.toolchain.api.xbase.action;
import com.facker.toolchain.base.utils.IOUtil;
import com.facker.toolchain.api.xbase.*;

import java.io.File;
import java.util.List;

public class MergeBridgeMainifest extends WorkerAction  {//敲定netconfig文件
    private XBridge xBridge;
    private XTarget xTarget;
    public MergeBridgeMainifest(XBridge xBridge, XTarget xTarget){
        this.xBridge = xBridge;
        this.xTarget = xTarget;
    }

    @Override
    public void action() throws Exception {
        /**
         * 源程序
         */
        ManifestEditor xTargetManifestEditor = new ManifestEditor(xTarget.getManifestFile());

        /**
         * 桥程序
         */
        ManifestEditor xBridgeManifestEditor = new ManifestEditor(xBridge.getManifestFile());

        /**
         * 基本修正
         */
        String applicationName = xTargetManifestEditor.getApplicationName();
        String packageName = xTargetManifestEditor.getPackagenName();
        //String lancherAppName = manifestMod.getLancherActivityName();
        if(!TextUtil.isEmpty(applicationName)){
            xTargetManifestEditor.insertMeta("APPLICATION_CLASS_NAME",applicationName);
        }
        xTargetManifestEditor.insertMeta("pkg",packageName);
        xTargetManifestEditor.modApplication(xBridgeManifestEditor.getApplicationName());//动态

        /**
         * 修改启动页面名称
         */
        if(!TextUtil.isEmpty(xTarget.getAppName())){
            xTargetManifestEditor.modLancherActivityLabel(xTarget.getAppName());
        }
        //manifestMod.modLancherActivityIntent();

        /**
         * 目标 application 名称
         */
        if(!TextUtil.isEmpty(xTarget.getAppName())){
            xTargetManifestEditor.modApplicationLabel(xTarget.getAppName());
        }

        /**
         * 修改安装包包名
         */
        if(!TextUtil.isEmpty(xTarget.getPackageName())){
            xTargetManifestEditor.modPkg(xTarget.getPackageName());
        }
        /**
         * 拷贝原有App项目
         */
        xTargetManifestEditor.copyApplicaionElements(xBridgeManifestEditor.getApplicationElement(),xTargetManifestEditor.getApplicationElement());

        /**
         * 权限
         */
        List<String> usesPermissions = xBridgeManifestEditor.getUsesPermissions();
        for (String usesPetmission :usesPermissions) {
            xTargetManifestEditor.insertUsesPermission(usesPetmission);
        }

        List<String> permissions = xBridgeManifestEditor.getPermissions();
        for (String permission :permissions) {
            xTargetManifestEditor.insertPermission(permission,null);
        }

        /**
         * netconfig 如果原来已经配置过了，保留原来的配置 ，如果没有的话，拷贝壳程序的配置名称 在 RESmerge环节会都该文件做相应处理
         */
        if(xTargetManifestEditor.getNetworkSecurityConfig()==null){
            String networkSecurityConfig = xBridgeManifestEditor.getNetworkSecurityConfig();
            xTargetManifestEditor.modNetworkSecurityConfig(networkSecurityConfig);
            //同时拷贝 bridge 目录下的NetworkSecurityConfig文件进去
            File networkSecurityConfigFile = new File(xTarget.getXMLDir(),networkSecurityConfig+".xml");
            if(!networkSecurityConfigFile.getParentFile().exists()){
                networkSecurityConfigFile.getParentFile().mkdir();
            }
            IOUtil.copyFile(new File(xBridge.getXMLDir(),networkSecurityConfig+".xml"),networkSecurityConfigFile);
        }
        xTargetManifestEditor.save();
    }
}

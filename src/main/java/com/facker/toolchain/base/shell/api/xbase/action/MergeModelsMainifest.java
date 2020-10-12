package com.facker.toolchain.base.shell.api.xbase.action;

import com.facker.toolchain.base.shell.api.xbase.ManifestEditor;
import com.facker.toolchain.base.shell.api.xbase.WorkerAction;
import com.facker.toolchain.base.shell.api.xbase.XModel;
import com.facker.toolchain.base.shell.api.xbase.XTarget;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.List;
import java.util.Map;

public class MergeModelsMainifest extends WorkerAction  {
    private List<XModel> xModels;
    private XTarget xTarget;
    public MergeModelsMainifest(List<XModel> xModels, XTarget xTarget){
        this.xModels = xModels;
        this.xTarget = xTarget;
    }
    @Override
    public void action() throws  Exception{
        for (XModel xModel: xModels) {
            ManifestEditor  xTargetManifestEditor = new ManifestEditor(xTarget.getManifestFile());
            ManifestEditor manifestModSdk = new ManifestEditor(xModel.getManifestFile());

            //拷贝
            xTargetManifestEditor.copyApplicaionElements(manifestModSdk.getApplicationElement(),xTargetManifestEditor.getApplicationElement());

            List<String> usesPermissions = manifestModSdk.getUsesPermissions();
            for (String usesPermission :usesPermissions) {
                xTargetManifestEditor.insertUsesPermission(usesPermission);
            }

            List<String> permissions = manifestModSdk.getPermissions();
            for (String permission :permissions) {
                xTargetManifestEditor.insertPermission(permission,"signature");
            }

            Map<String,String> localParamsMap = xModel.getParams();
            if(localParamsMap!=null){
                for (Map.Entry<String,String> localParamEntry:localParamsMap.entrySet() ) {
                    System.out.println("key "+localParamEntry.getKey() +" value "+localParamEntry.getValue());
                    xTargetManifestEditor.coverMeta(localParamEntry.getKey(),localParamEntry.getValue());
                }
            }
            //对manifestd的修改
            String doamain = manifestModSdk.getPackagenName();
//            xTargetManifestEditor.modModelProviderAuthorities(doamain,xTargetManifestEditor.getPackagenName());
            //xml format
            formatXml(xTargetManifestEditor.getManifestElement(),doamain,xTargetManifestEditor.getPackagenName());


            xTargetManifestEditor.save();
        }
    }

    public void formatXml(Element node,String modelDomain,String targetPkg){
        //当前节点的名称、文本内容和属性
        System.out.println("当前节点名称："+node.getName());//当前节点名称
        System.out.println("当前节点的内容："+node.getTextTrim());//当前节点名称
        List<Attribute> listAttr=node.attributes();//当前节点的所有属性的list
        for(Attribute attr:listAttr){//遍历当前节点的所有属性
            String name=attr.getName();//属性名称
            String value=attr.getValue();//属性的值
            System.out.println("属性名称："+name+"属性值："+value);
            if(value.contains(modelDomain)){
                value = value.replace(modelDomain,targetPkg);
                attr.setValue(value);
            }
        }
        //递归遍历当前节点所有的子节点
        List<Element> listElement=node.elements();//所有一级子节点的list
        for(Element e:listElement){//遍历所有一级子节点
            this.formatXml(e,modelDomain,targetPkg);//递归
        }
    }
}

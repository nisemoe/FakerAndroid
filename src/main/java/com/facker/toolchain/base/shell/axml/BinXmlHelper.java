package com.facker.toolchain.base.shell.axml;

import com.facker.toolchain.base.shell.axml.struct.AttributeData;

import java.util.List;

public class BinXmlHelper {

    /**
     * 查找最佳插入位置
     * @param editor 编辑器
     * @return
     */
    private static int getBestPositionName(BinXmlParser editor){
        List<AttributeData> attrs = editor.memListAttr("application");
        int label = 0, icon = 0;
        for (int i = 0; i < attrs.size(); i++) {
            final AttributeData data = attrs.get(i);
            final String name = editor.getString(data.name);
            if("label".equals(name)){
                label = i;
            }
            if("icon".equals(name)){
                icon = i;
            }
        }
        return Math.max(label, icon);
    }

    /**
     * 插入Application Name
     * @param editor
     * @param attrValue
     */
    public static void insertApplicationName(BinXmlParser editor, String attrValue){
        int sId = getApplicationNameId(editor);
        if(sId != -1){
            editor.setStringById(sId, attrValue);
            return;
        }
        int newPos = editor.lookingForGoodPosition(editor.memListAttr("application") , "name");
//        int bestPosition = getBestPositionName(editor) + 1;
        editor.memAddAttr("application", "name", attrValue, newPos);
    }

    /**
     * 获取已有的Application Name Id
     * @param editor
     * @return 如果有返回id，没有返回-1
     */
    public static int getApplicationNameId(BinXmlParser editor){
        List<AttributeData> attrs = editor.memListAttr("application");
        for (int i = 0; i < attrs.size(); i++) {
            AttributeData data = attrs.get(i);
            final String name = editor.getString(data.name);
            if("name".equals(name)){
                return data.valueString;
            }
        }
        return -1;
    }

    public static void autoCreateNode(BinXmlParser editor, String tag, String attrName, AttributeData data){
        editor.memAddAttr(tag, data,
                editor.lookingForGoodPosition(editor.memListAttr(tag), attrName));
    }

    public static void modifyApplicationLabel(BinXmlParser parser, String label){
        AttributeData data = parser.createAttributeData("label", label);
        parser.memModifyAttr("application", null, "label", data);
    }

    public static int readTargetPlatformCode(BinXmlParser parser){
        String targetSdk = parser.memReadAttr("uses-sdk", null, "targetSdkVersion");
        if(targetSdk != null){
            return Integer.parseInt(targetSdk);
        }
        return 0;
    }
}

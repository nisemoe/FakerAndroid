package com.facker.toolchain.api.xbase;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicMerge {

    public static void main(String[] args) {
        try {
            mergePublicXml(new File("modelpublic.xml"),new File("targetpublic.xml"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public static void mergePublicXml(File oFile, File targetFile) throws DocumentException {

        ValuesMod oValuesMod = new ValuesMod(oFile);
        ValuesMod targetValuesMod = new ValuesMod(targetFile);

        Map<String,ID> minInjectIDMap = getMinInjectID(oValuesMod,targetValuesMod);
        List<Element> oElements = oValuesMod.getResourcesElement().elements();
        oElements.forEach(item-> {
            String type = item.attributeValue("type");
            ID minID = minInjectIDMap.get(type);
            item.attribute("id").setValue(minID.getID());
            targetValuesMod.copyValueElement(item,targetValuesMod.getResourcesElement());
            //覆盖原有的最小值
            minInjectIDMap.put(type,new ID(minID.getTypeIntegerValue(),minID.getIdIntegerValue()+1));
        });
        try {
            targetValuesMod.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取要植入的最小植入类型加ID值的键值对
     * @param oValuesMod
     * @param targetValuesMod
     * @return
     */
    public static  Map<String, ID> getMinInjectID(ValuesMod oValuesMod,ValuesMod targetValuesMod) {

        HashMap<String,ID> maxTargetIDMap = new HashMap<>();
        //遍历原来的文件找到最大的ID
        List<Element> targetValueElements = targetValuesMod.getResourcesElement().elements(); //od 计算

        //找出元xml文件中各个类型最大的ID集合
        targetValueElements.forEach(item->{
            String type = item.attributeValue("type");
            String id = item.attributeValue("id");
            ID formatId = new ID(id);
            if(maxTargetIDMap.get(type)==null){
                maxTargetIDMap.put(type,formatId);
            }else{
                int maxTargetIdInteger = maxTargetIDMap.get(type).getIdIntegerValue();
                if(formatId.getIdIntegerValue()>maxTargetIdInteger){
                    maxTargetIDMap.put(type,formatId);
                }
            }
        });
        //test
        int maxTypeValue = 0;
        for (Map.Entry<String,ID> maxTargetIDMapEnry :maxTargetIDMap.entrySet())  {
            System.out.println("type ="+maxTargetIDMapEnry.getKey() +",ID="+maxTargetIDMapEnry.getValue().getID());
            if(maxTargetIDMapEnry.getValue().getTypeIntegerValue()>maxTypeValue){
                maxTypeValue = maxTargetIDMapEnry.getValue().getTypeIntegerValue();
            }
        }
        System.out.println("最大资源类型int:"+maxTypeValue +",hex:"+String.format("%04x",Integer.valueOf(encodeHEX(maxTypeValue),16)));


        int minTypeVlue = maxTypeValue+1;
        Map<String,ID> minInjectID = new HashMap<>();
        //遍历oxm,并在target基础上做加以操作
        List<Element> oValueElements = oValuesMod.getResourcesElement().elements(); //od 计算
        for (Element element : oValueElements) {
            String type = element.attributeValue("type");
            if(minInjectID.get(type)==null){
                if(maxTargetIDMap.get(type)!=null){
                    ID targetMaxId = maxTargetIDMap.get(type);
                    ID id = new ID(targetMaxId.getTypeIntegerValue(),targetMaxId.getIdIntegerValue()+1);
                    minInjectID.put(type,id);

                } else {
                    ID id = new ID(minTypeVlue,0);
                    minInjectID.put(type,id);
                    System.out.println("不存在的元素类型"+type);
                    minTypeVlue++;
                }
            }
        };
        for (Map.Entry<String,ID> minInjectIDIDMapEnry :minInjectID.entrySet())  {
            System.out.println(" 结果集合 type ="+minInjectIDIDMapEnry.getKey() +",ID="+minInjectIDIDMapEnry.getValue().getID());
            if(minInjectIDIDMapEnry.getValue().getTypeIntegerValue()>maxTypeValue){
                maxTypeValue = minInjectIDIDMapEnry.getValue().getTypeIntegerValue();
            }
        }
        //如果有就增加，如果没有就在原来的基础上创建
        return minInjectID;
    }

    public static  class ID {

        String typeHexValue;

        String idHexValue;

        int typeIntegerValue;

        int idIntegerValue;

        ID(int typeIntegerValue,int idIntegerValue) {
            this.typeIntegerValue = typeIntegerValue;
            this.idIntegerValue = idIntegerValue;
            this.typeHexValue = String.format("%04x",Integer.valueOf(encodeHEX(typeIntegerValue),16));
            this.idHexValue = String.format("%04x",Integer.valueOf(encodeHEX(idIntegerValue),16));
        }
        ID(String typeHexValue, String idHexValue) {
            this.typeIntegerValue = typeIntegerValue;
            this.idIntegerValue = idIntegerValue;
            this.typeIntegerValue = decodeHEX(typeHexValue);
            this.idIntegerValue  = decodeHEX(idHexValue);
        }

        ID(String id) {//0x7f0b0000
            String tmpId = id.replace("0x","");
            this.typeHexValue = tmpId.substring(0,4);
            this.idHexValue = tmpId.substring(4,8);
            this.typeIntegerValue = decodeHEX(typeHexValue);
            this.idIntegerValue  = decodeHEX(idHexValue);
        }
        public String getID(){
            return "0x"+typeHexValue+idHexValue;
        }

        public String getTypeHexValue() {
            return typeHexValue;
        }

        public String getIdHexValue() {
            return idHexValue;
        }

        public int getTypeIntegerValue() {
            return typeIntegerValue;
        }

        public void setTypeIntegerValue(int typeIntegerValue) {
            this.typeIntegerValue = typeIntegerValue;
        }

        public int getIdIntegerValue() {
            return idIntegerValue;
        }

        public void setIdIntegerValue(int idIntegerValue) {
            this.idIntegerValue = idIntegerValue;
        }
    }

    //將10進制轉換為16進制
    public static String encodeHEX(Integer numb){

        String hex= Integer.toHexString(numb);
        return hex;

    }


    //將16進制字符串轉換為10進制數字
    public static int decodeHEX(String hexs){
        BigInteger bigint=new BigInteger(hexs, 16);
        int numb=bigint.intValue();
        return numb;
    }
}

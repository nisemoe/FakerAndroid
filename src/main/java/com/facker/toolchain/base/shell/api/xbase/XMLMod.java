package com.facker.toolchain.base.shell.api.xbase;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class XMLMod {
    /**
     * manifest 路径
     */
    File manifestFile;

    /**
     *
     */
    Document document;


    /**
     * manifest
     */
    Element resourcesElement;


    public XMLMod(File manifestFile) throws DocumentException {
        this.manifestFile = manifestFile;
        SAXReader reader = new SAXReader();
        document = reader.read(manifestFile);
        resourcesElement = document.getRootElement();
    }
    public Element getResourcesElement(){
        return  resourcesElement;
    }


    public void copyNetConfig(Element elementFrom,Element elementTo) {//TODO 如果是入口Activity的话则跳过
        //拷贝netconfig 文件
        if("network-security-config".equals(resourcesElement.getName())){
            Element domainFromConfigElement = elementFrom.element("domain-config");
            List<Element> domainElements = domainFromConfigElement.elements("domain");
            domainElements.forEach(item->{
                Element copy = item.createCopy();
                elementTo.element("domain-config").add(copy);
            });
        }
    }
    //拷贝节点下的所有元素
    public void copyValueElements(Element elementFrom,Element elementTo) {//TODO 如果是入口Activity的话则跳过
        if("paths".equals(resourcesElement.getName())){
            List<Element> elementsFrom = elementFrom.elements();
            List<Element> elementsTo = elementTo.elements();
            for (Element elementItemFrom : elementsFrom) {//TODO 重名舍弃
                String fromName = elementItemFrom.attributeValue("name");
                if(!isNameRepeat(elementsTo, fromName)){
                    Element copy = elementItemFrom.createCopy();
                    elementTo.add(copy);
                    System.out.println("name 非重复元素:"+elementItemFrom.attributeValue("name"));
                }else{
                    System.out.println("name 重复元素:"+elementItemFrom.attributeValue("name"));
                }
            }
        }
        //拷贝netconfig 文件
        if("network-security-config".equals(resourcesElement.getName())){
            Element domainFromConfigElement = elementFrom.element("domain-config");
            List<Element> domainElements = domainFromConfigElement.elements("domain");
            domainElements.forEach(item->{
                Element copy = item.createCopy();
                elementTo.element("domain-config").add(copy);
            });


        }
    }


    private boolean isNameRepeat(List<Element> elementsTo, String fromName) {
        for (Element elementItemTo: elementsTo) {
            String toName = elementItemTo.attributeValue("name");
            if(toName.equals(fromName)){
                return true;
            }
        }
        return false;
    }


    public void save() throws IOException {
        OutputFormat xmlFormat = OutputFormat.createPrettyPrint();
        xmlFormat.setEncoding("UTF-8");
        // 设置换行
        xmlFormat.setNewlines(true);
        // 生成缩进
        xmlFormat.setIndent(true);
        // 使用4个空格进行缩进, 可以兼容文本编辑器
        xmlFormat.setIndent("    ");

        XMLWriter writer = new XMLWriter(new FileWriter(manifestFile), xmlFormat);
        writer.write(document);
        writer.close();
    }
}


package com.facker.toolchain.api.xbase;

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

public class NetworkSecurityConfigEditor {
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


    public NetworkSecurityConfigEditor(File manifestFile) throws DocumentException {
        this.manifestFile = manifestFile;
        SAXReader reader = new SAXReader();
        document = reader.read(manifestFile);
        resourcesElement = document.getRootElement();
    }
    public Element getResourcesElement(){
        return  resourcesElement;
    }
    public Element getbaseConfigElement(){
        return resourcesElement.element("base-config");
    }
    public Element getDomainConfigElement (){
        return resourcesElement.element("base-config");
    }

    public List<Element> getDomainElements (){
        return resourcesElement.element("base-config").elements("domain");
    }

    public void copyElements(List<Element> elements,Element toElement) {
        for (Element elementItemFrom : elements) {//TODO 重名舍弃
            Element copy = elementItemFrom.createCopy();
            toElement.add(copy);
        }
    }

    public void formatElement() {
        if(resourcesElement.element("base-config")==null) {
            Element baseConfingElement = resourcesElement.addElement("base-config");
            baseConfingElement.addAttribute("cleartextTrafficPermitted", "true");
        }else{
            if(!resourcesElement.element("base-config").attributeValue("cleartextTrafficPermitted").equals("true")){
                resourcesElement.element("base-config").attribute("cleartextTrafficPermitted").setValue("true");
            }
        }

        if(resourcesElement.element("domain-config")==null){
            Element domainConfigElement = resourcesElement.addElement("domain-config");
            Element domainElement = domainConfigElement.addElement("domain");
            domainElement.addAttribute("includeSubdomains","true");
            domainElement.addText("ianpei.com");
            Element  trustAnchorsElement = domainConfigElement.addElement("trust-anchors");
            Element certificatesElementUSer = trustAnchorsElement.addElement("certificates");
            certificatesElementUSer.addAttribute("src","user");
            Element certificatesElementSystem = trustAnchorsElement.addElement("certificates");
            certificatesElementSystem.addAttribute("src","system");
        }else{
            Element domainConfigElement = resourcesElement.element("domain-config");
            Element trustAnchorsElement = domainConfigElement.element("trust-anchors");
            if(trustAnchorsElement==null){
                trustAnchorsElement = domainConfigElement.addElement("trust-anchors");
                Element certificatesElementUSer = trustAnchorsElement.addElement("certificates");
                certificatesElementUSer.addAttribute("src","user");
                Element certificatesElementSystem = trustAnchorsElement.addElement("certificates");
                certificatesElementSystem.addAttribute("src","system");
            }
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


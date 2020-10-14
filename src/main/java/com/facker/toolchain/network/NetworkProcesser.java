package com.facker.toolchain.network;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;

public class NetworkProcesser {
    final static String networkConfig = "ianpei_network_security_config";
    public static void rewriteNetworkConfig(File manifest) throws Exception{
        if(manifest == null || !manifest.exists()){
            throw new RuntimeException("Manifest File Not Found :NetworkProcesser");
        }
        SAXReader reader = new SAXReader();
        Document doc = reader.read(manifest);
        Element root = doc.getRootElement();
        int targetSdkVersion = Integer.parseInt(root.attributeValue("platformBuildVersionCode", "0"));
        {
            Element applicationElement = null;
            for (Iterator<Element> it = root.elementIterator(); it.hasNext(); ) {
                Element e = it.next();
                if ("application".equals(e.getName())){
                    applicationElement = e;
                    for (Iterator<Attribute> a = e.attributeIterator(); a.hasNext(); ){
                        Attribute attribute = a.next();
                        if("networkSecurityConfig".equals(attribute.getName())){
                            System.out.println("networkSecurityConfig Exist!");
                            String value = attribute.getStringValue();
                            String xmlFilename = value.replace("@xml","")+".xml";
                            File xmlFile = new File(manifest.getParentFile(),"res/xml/"+xmlFilename);
                            SAXReader xmlFileReader = new SAXReader();
                            Document xmlFileReaderDoc = reader.read(xmlFile);
                            if("true".equals(xmlFileReaderDoc.getRootElement().element("base-config").attributeValue("cleartextTrafficPermitted"))){
                                return;
                            }

                        }
                    }
                }
            }

            if(applicationElement != null){
                Attribute isSelfAttr = applicationElement.attribute("networkSecurityConfig");
                if(isSelfAttr!=null){
                    isSelfAttr.setValue(String.format("@xml/%s", networkConfig));
                }else{
                    applicationElement.addAttribute("android:networkSecurityConfig", String.format("@xml/%s", networkConfig));
                }
                XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(manifest));
                xmlWriter.write(doc);
                xmlWriter.close();
                System.out.println("copy networkSecurityConfig ok");
            }
        }
    }
    public static void main(String[] args) throws Exception {
        rewriteNetworkConfig(new File("G:\\out\\app-debug\\AndroidManifest.xml"));
    }
}

package com.facker.toolchain.axml;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class DefaultIdHolder implements IdHolder {

    public final Map<Integer, String> intMap = new HashMap<>();
    public final Map<String, Integer> strMap = new HashMap<>();
    private static volatile DefaultIdHolder INSTANCE;

    public static void main(String[] args) {
        DefaultIdHolder.getInstance();
    }

    public static DefaultIdHolder getInstance()  {
        if(INSTANCE == null){
            synchronized (DefaultIdHolder.class){
                if(INSTANCE == null){
                    try {
                        INSTANCE = new DefaultIdHolder();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return INSTANCE;
    }

    public DefaultIdHolder() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document document = documentBuilder.parse(new File("public.xml"));
        NodeList list = document.getElementsByTagName("public");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            NamedNodeMap nodeMap = node.getAttributes();
            if(nodeMap.getLength() > 0 && nodeMap.getNamedItem("type") != null
            && nodeMap.getNamedItem("type").getNodeValue().equalsIgnoreCase("attr")
            && nodeMap.getNamedItem("id") != null){
                int value = Integer.parseInt(nodeMap.getNamedItem("id").getNodeValue().substring(2), 16);
                String key = nodeMap.getNamedItem("name").getNodeValue();
                intMap.put(value, key);
                strMap.put(key, value);
            }
        }
    }

    @Override
    public String get(int id) {
        return intMap.get(id);
    }

    @Override
    public Integer get(String str) {
        return strMap.get(str);
    }
}

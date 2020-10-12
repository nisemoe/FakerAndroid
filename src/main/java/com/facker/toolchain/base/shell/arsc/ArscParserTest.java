package com.facker.toolchain.base.shell.arsc;

import com.facker.toolchain.base.beta.PathUtil;

import java.io.IOException;


public class ArscParserTest {
    ArscParser parser;

    public static void main(String[] args) throws IOException {
        ArscParserTest test = new ArscParserTest();
        test.setUp();
        makeXmlIn(test.parser,"hellc");
        test.tearDown();
    }

    void setUp() throws IOException {
        parser = new ArscParser(PathUtil.inputFile(""));
    }

    public static int makeXmlIn(ArscParser parser, String xmlName) throws IOException {
        String xmlStr = String.format("res/xml/%s", xmlName);
        final int data = parser.mStringPool.getOrCreateString(xmlStr);
        if(parser.tablePackages.size() > 0){
            TablePackageType table = parser.tablePackages.get(0);
            int pos = table.hasSection("xml");
            if(pos == -1){
                System.out.println("Creating Xml Section");
                int id = table.s1.getOrCreateString("xml") + 1;
                ArscHeader header = new ArscHeader((short)ResType.RES_TABLE_TYPE_SPEC_TYPE.value, (short) 16, 0);
                ArscResTypeSpec spec = new ArscResTypeSpec((byte)id, (byte)0, 0, table.s1, header);
                ArscHeader bodyHeader = new ArscHeader((short)ResType.RES_TABLE_TYPE_TYPE.value, (short)84, 0);
                ArscTableType tableType = new ArscTableType(id, 0, 0,
                        ArscResTableConfig.createDefaultConfig(), table.s2, table.mPackageId, bodyHeader);
                spec.children.add(tableType);
                table.specMap.put(id, spec);
            }
            int newPos = table.s1.getOrCreateString("xml");
            ArscResTypeSpec spec = table.specMap.values()
                    .stream()
                    .filter(x -> x.id == newPos + 1)
                    .limit(1)
                    .findFirst()
                    .orElseThrow(()-> new RuntimeException("Create Section Failed..."));
            spec.typespecEntries.add(0);
            int index = table.s2.getOrCreateString(xmlName);
            int id = spec.children.get(0)
                    .put(new ArscResTableEntry(index, 0, null,
                            new ArscResStringPoolRef(3, data, 0)));
            return id;
        }
        throw new RuntimeException("Not A Good Arsc File");
    }

//    void test() throws IOException {
//        TablePackageType table = parser.tablePackages.get(0);
//        int pos = table.hasSection("xml");
//        table.specMap.entrySet().stream()
//        .filter(mybatis-config.xml -> mybatis-config.xml.getKey() == pos + 1)
//        .limit(1)
//        .map(Map.Entry::getValue)
//        .flatMap(mybatis-config.xml -> mybatis-config.xml.children.stream())
//        .flatMap(mybatis-config.xml -> mybatis-config.xml.entries.stream())
//        .forEach(mybatis-config.xml -> System.out.println(table.s2.getString(mybatis-config.xml.index)));

//
//        ArscParser arscParser =
//                new ArscParser(new File("resources0.arsc"));
//    }

    void tearDown() { }
}
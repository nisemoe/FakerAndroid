package com.facker.toolchain.base.shell.arsc;

import com.google.common.base.Charsets;
import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;
import com.facker.toolchain.base.shell.axml.ByteUtil;
import com.facker.toolchain.base.shell.axml.struct.StringPoolImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class TablePackageType extends ArscChunk {

    final int mPackageId;
    int currentPackageId;
    byte name[];
    ArscHeader arscHeader1, arscHeader2;
    int typeString, lastPublicType, keyStrings, lastPublicKey;
    public final StringPoolImpl s1, s2;
    public final LinkedHashMap<Integer, ArscResTypeSpec> specMap = new LinkedHashMap<>();
    public final String _cacheName;
    public final int skipNum;
    public TablePackageType(LittleEndianDataInputStream stream,
                            ArscHeader header) throws IOException {
        super(header);
        mPackageId = stream.readInt();
        currentPackageId = mPackageId << 24;
        name = new byte[256];
        stream.readFully(name);
        _cacheName = new String(name, Charsets.UTF_16LE);
        typeString = stream.readInt();
        lastPublicType = stream.readInt();
        keyStrings = stream.readInt();
        lastPublicKey = stream.readInt();
        System.out.printf("RootPkg: 0x%08x %d, %d, %d, %d, %d, %s\n",
                mPackageId,
                typeString,
                lastPublicKey, keyStrings,
                lastPublicType, mHeader.mHeadSize, _cacheName);
        skipNum = mHeader.mHeadSize - 256 - 20 - 8;//减去头大小
        stream.skipBytes(skipNum);
         arscHeader1 = new ArscHeader(stream);
        if(arscHeader1.mType != ResType.RES_STRING_POOL_TYPE.value){
            throw new RuntimeException("Line:26 Unknow Info");
        }
        s1 = new StringPoolImpl(stream, arscHeader1);
         arscHeader2 = new ArscHeader(stream);
        if(arscHeader2.mType != ResType.RES_STRING_POOL_TYPE.value){
            throw new RuntimeException("Line:30 Unknow Info");
        }
        System.out.printf("s1: %s, s2: %s\n",
                arscHeader1.toString(),
                arscHeader2.toString());
        s2 = new StringPoolImpl(stream, arscHeader2);
        System.out.printf("s1-size:%d, s2-size:%d\n", s1.getStringCount(), s2.getStringCount());
        int left = mHeader.mResSize  - arscHeader1.mResSize
                -arscHeader2.mResSize
                -mHeader.mHeadSize;
        ArscResTypeSpec currentSpec = null;
        while (left > 0 ){
            ArscHeader h = new ArscHeader(stream);
            switch (h.mType){
//                RES_TABLE_TYPE_SPEC_TYPE    ( 0x0202),
                case 0x202:
                    currentSpec = new ArscResTypeSpec(stream, h, s1);
                    specMap.put((int)currentSpec.id, currentSpec);
                    break;
//                RES_TABLE_TYPE_TYPE         ( 0x0201),
                case 0x201:
                    if(currentSpec != null)
                        currentSpec.addChild(new ArscTableType(stream, h, this, s2));
                    else
                        throw new RuntimeException("Current Spec Not Inited");
                    break;
                default:
                    throw new RuntimeException(String.format("Here Can't Process Type:%mybatis-config.xml", h.mType));
//                    System.out.printf("Found Some Chunk Can't Process" +
//                            "type:0x%08x, skiped", h.mType);
//                    stream.skipBytes(h.mResSize - 8);

            }
            left -= h.mResSize;
        }
    }

    public int hasSection(String s){
        return s1.findStringId(s);
    }

    public int lastId(){
        return specMap.keySet()
                .stream()
                .max((x, y)-> x - y)
                .orElse(0);
    }

    @Override
    public byte[] build() throws IOException {
        byte s1Content[] = s1.build();
        byte strPoolData[] = ByteUtil.byteConcatEx(s1Content, s2.build());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        LittleEndianDataOutputStream stream = new LittleEndianDataOutputStream(outputStream);
        byte content[] = null;
        for (Map.Entry<Integer, ArscResTypeSpec> specEntry : specMap.entrySet()) {
            ArscResTypeSpec v = specEntry.getValue();
            content = ByteUtil.byteConcatEx(content, v.build());
        }
        mHeader.mResSize = content.length + strPoolData.length + mHeader.mHeadSize;
        mHeader.build(stream);//header part
        stream.writeInt(mPackageId);
        stream.write(name);
        stream.writeInt(typeString);
        stream.writeInt(lastPublicType);
        stream.writeInt(typeString + s1Content.length);
        stream.writeInt(lastPublicKey);
        stream.write(new byte[skipNum]);
        stream.write(strPoolData);
        stream.write(content);
        return outputStream.toByteArray();
    }
}

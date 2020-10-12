package com.facker.toolchain.base.shell.axml.struct;


import com.facker.toolchain.base.shell.axml.ByteUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class StartTagChunk implements Chunk {

    public int type;
    public int size;
    public int lineNumber;
    public int unknown;
    public int uri;
    public int name;
    public int flag;
    public int classAttr;
    public List<AttributeData> attrList;

    public String optName;

    public int offset;

    public void makeOpt(StringPool stringPool){
        optName = stringPool.getString(name);
        for (AttributeData data : attrList) {
            data.makeOpt(stringPool);
        }
    }

    @Override
    public String toString() {
        return String.format("Start: %s,count: %d atts: %s",
                optName == null ? "" : optName, attrList.size(), Arrays.toString(attrList.toArray()));
    }

    public StartTagChunk() {
        type = ChunkTypeNumber.CHUNK_STARTTAG;
        lineNumber = 0;
        unknown = 0;
        flag = 0;
        classAttr = 0;
        flag = 0x00140014;
    }

    private byte[] getAttrBytes() {
        byte result[] = new byte[0];
        for (AttributeData data : attrList) {
            result = ByteUtil.byteConcatEx(result, data.getByte());
        }
        return result;
    }

    public byte[] getChunkByte() {
        byte[] bytes = new byte[getLen()];
        bytes = ByteUtil.byteConcat(bytes, ByteUtil.int2Byte(type), 0);
        bytes = ByteUtil.byteConcat(bytes, ByteUtil.int2Byte(getLen()), 4);
        bytes = ByteUtil.byteConcat(bytes, ByteUtil.int2Byte(lineNumber), 8);
        bytes = ByteUtil.byteConcat(bytes, ByteUtil.int2Byte(unknown), 12);
        bytes = ByteUtil.byteConcat(bytes, ByteUtil.int2Byte(uri), 16);
        bytes = ByteUtil.byteConcat(bytes, ByteUtil.int2Byte(name), 20);
        bytes = ByteUtil.byteConcat(bytes, ByteUtil.int2Byte(flag), 24);
        bytes = ByteUtil.byteConcat(bytes, ByteUtil.int2Byte(attrList.size()), 28);
        bytes = ByteUtil.byteConcat(bytes, ByteUtil.int2Byte(classAttr), 32);
        bytes = ByteUtil.byteConcat(bytes, getAttrBytes(), 36);
        return bytes;
    }

    public int getLen() {
        return 36 + attrList.size() * 20;
    }

    public static StartTagChunk createChunk(int name, int uri, List<AttributeData> data) {
        StartTagChunk chunk = new StartTagChunk();
        chunk.name = name;
        chunk.uri = uri;
        chunk.attrList = data;
        chunk.size = chunk.getLen();
        return chunk;
    }

    public static StartTagChunk createChunk(byte[] byteSrc, int offset) {
        StartTagChunk chunk = new StartTagChunk();

        chunk.offset = offset;
        chunk.type = ByteUtil.byte2intEx(byteSrc, 0);
        chunk.size = ByteUtil.byte2intEx(byteSrc, 4);
        chunk.lineNumber = ByteUtil.byte2intEx(byteSrc, 8);
        chunk.unknown = ByteUtil.byte2intEx(byteSrc, 12);
        chunk.uri = ByteUtil.byte2intEx(byteSrc, 16);
        chunk.name = ByteUtil.byte2intEx(byteSrc, 20);
        chunk.flag = ByteUtil.byte2intEx(byteSrc, 24);
        int attCount = ByteUtil.byte2intEx(byteSrc, 28);
        chunk.classAttr = ByteUtil.byte2intEx(byteSrc, 32);
        chunk.attrList = new ArrayList<AttributeData>();
        for (int i = 0; i < attCount; i++) {
            AttributeData attrData = new AttributeData();
            for (int j = 0; j < 5; j++) {
                int current = ByteUtil.byte2intEx(byteSrc, 36 + i * 20 + j * 4);
                attrData.offset = offset + 36 + i * 20;
                switch (j) {
                    case 0:
                        attrData.nameSpaceUri = current;
                        break;
                    case 1:
                        attrData.name = current;
                        break;
                    case 2:
                        attrData.valueString = current;
                        break;
                    case 3:
                        attrData.type = current;
                        break;
                    case 4:
                        attrData.data = current;
                        break;
                }
            }
            chunk.attrList.add(attrData);
        }
        return chunk;
    }

}

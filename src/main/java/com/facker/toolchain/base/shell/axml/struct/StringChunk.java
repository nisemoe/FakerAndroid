package com.facker.toolchain.base.shell.axml.struct;

import com.facker.toolchain.base.shell.axml.ByteUtil;

import java.util.ArrayList;


public class StringChunk implements StringPool {
    public static final int IS_UTF8 = 1 << 8;
    public byte[] type;
    public byte[] size;
    public byte[] strCount;
    public byte[] styleCount;
    public byte[] mFlag;
    public boolean is_utf8;
    public byte[] strPoolOffset;
    public byte[] stylePoolOffset;
    public byte[] strOffsets;
    public byte[] styleOffsets;
    public byte[] strPool;
    public byte[] stylePool;

    public ArrayList<String> mStringContentList;


    public int getStringPoolSize(){
        return ByteUtil.byte2int(size);
    }

    public byte[] getByte(ArrayList<String> strList){
        byte[] strB = getStrListByte(strList);
        byte[] src = new byte[0];
        src = ByteUtil.addByte(src, type);//添加类型
        src = ByteUtil.addByte(src, size);//添加大小
        src = ByteUtil.addByte(src, ByteUtil.int2Byte(strList.size()));//添加字符串数量
        src = ByteUtil.addByte(src, styleCount);//添加样式数量
        src = ByteUtil.addByte(src, new byte[]{0, 0, 0, 0});//添加Flag
        src = ByteUtil.addByte(src, strPoolOffset);
        src = ByteUtil.addByte(src, stylePoolOffset);

        byte[] strOffsets = new byte[0];
        ArrayList<String> convertList = convertStrList(strList);

        int len = 0;
        for(int i=0;i<convertList.size();i++){
            strOffsets = ByteUtil.addByte(strOffsets, ByteUtil.int2Byte(len));
            len += (convertList.get(i).length()+4);
        }

        src = ByteUtil.addByte(src, strOffsets);

        int newStyleOffsets = src.length;

        src = ByteUtil.addByte(src, styleOffsets);

        int newStringPools = src.length;

        src = ByteUtil.addByte(src, strB);

        src = ByteUtil.addByte(src, stylePool); 
        
        if(styleOffsets != null && styleOffsets.length > 0){
            src = ByteUtil.replaceBytes(src, ByteUtil.int2Byte(newStyleOffsets), 28+strList.size()*4);
        }

        src = ByteUtil.replaceBytes(src, ByteUtil.int2Byte(newStringPools), 20);

        if(src.length % 4 != 0){
            src = ByteUtil.addByte(src, new byte[]{0,0});
        }

        src = ByteUtil.replaceBytes(src, ByteUtil.int2Byte(src.length), 4);

        return src;
    }

    public int getLen(){
        return type.length+size.length+strCount.length+styleCount.length+
                mFlag.length+strPoolOffset.length+stylePoolOffset.length+
                strOffsets.length+styleOffsets.length+strPool.length+stylePool.length;
    }

    public String getStringById(int id){
        try {
            return mStringContentList.get(id);
        }catch (IndexOutOfBoundsException e){
            return "";
        }
    }

    public byte[] modStringChunk(byte[] bytes){
        byte[] newStrChunkB = getByte(mStringContentList);
        byte[] removed = ByteUtil.removeByte(bytes, 8, ByteUtil.byte2int(size));
        byte[] result = ByteUtil.insertByte(removed, 8, newStrChunkB);
        return result;
    }

    private byte[] getStrListByte(ArrayList<String> strList){
        byte[] src = new byte[0];
        ArrayList<String> stringContentList = convertStrList(strList);
        for(int i=0;i<stringContentList.size();i++){
            byte[] tempAry = new byte[0];
            short len = (short)(stringContentList.get(i).length()/2);
            byte[] lenAry = ByteUtil.shortToByte(len);
            tempAry = ByteUtil.addByte(tempAry, lenAry);
            tempAry = ByteUtil.addByte(tempAry, stringContentList.get(i).getBytes());
            tempAry = ByteUtil.addByte(tempAry, new byte[]{0,0});
            src = ByteUtil.addByte(src, tempAry);
        }
        return src;
    }

    private ArrayList<String> convertStrList(ArrayList<String> stringContentList){
        ArrayList<String> destList = new ArrayList<String>(stringContentList.size());
        for(String str : stringContentList){
            byte[] temp = str.getBytes();
            byte[] src = new byte[temp.length*2];
            for(int i=0;i<temp.length;i++){
                src[i*2] = temp[i];
                src[i*2+1] = 0;
            }
            destList.add(new String(src));
        }
        return destList;
    }

    @Override
    public byte[] getBytes() {
        return getByte(mStringContentList);
    }

    public int getOldSize() {
        return ByteUtil.byte2int(size);
    }

    @Override
    public int getStringCount() {
        return mStringContentList.size();
    }

    @Override
    public int findStringId(String content) {
        return 0;
    }

    @Override
    public int getOrCreateString(String content) {
        return 0;
    }

    @Override
    public String getString(int id) {
        return getStringById(id);
    }

    @Override
    public void setStringById(int id, String content) {

    }
}

package com.facker.toolchain.axml.struct;

import com.google.common.io.LittleEndianDataOutputStream;
import com.facker.toolchain.arsc.ArscHeader;
import com.facker.toolchain.arsc.BuildAble;
import com.facker.toolchain.arsc.StringPoolReader;
import com.facker.toolchain.axml.ByteUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StringPoolImpl implements StringPool, StringPoolReader, BuildAble {
    private final int mType;
    private final List<String> mContents = new ArrayList<String>();
    private final List<Integer> mStyleIds = new ArrayList<Integer>();
    private ArscHeader header = null;

    public StringPoolImpl(byte[] src){
        this(src, true);
    }

    public StringPoolImpl(DataInput in, ArscHeader header) throws IOException {
        mType = header.mType;
        this.header = header;
        int sCount = in.readInt();
        int iStyleCount = in.readInt();
        int mFlag = in.readInt();
        boolean isUtf8 = (mFlag & IS_UTF8) != 0;
        int iStrOffset = in.readInt();//8 + sCount*4 + 20
        int iStyOffset = in.readInt();
        if(iStyOffset != 0){
            System.err.println("Find Style!!!");
        }
        for (int i = 0; i < iStyleCount; i++) {
            int id = in.readInt();
            mStyleIds.add(id);
        }
        byte[] StringChunk = new byte[header.mResSize - iStrOffset];
        List<Integer> strOffsets = new ArrayList<>();
        for (int i = 0; i < sCount; i++) {
            strOffsets.add(in.readInt());
        }
        in.readFully(StringChunk);
        add(StringChunk, strOffsets, isUtf8);
    }

    private void add(byte[] StringChunk, List<Integer> strOffsets, boolean isUtf8){
        mContents.clear();
        mContents.addAll(strOffsets.stream().map( v -> {
            if(isUtf8){
                int highbit = 0x80;
                int len = (StringChunk[v] & 0x0ff);
                int skip = 1;
                if((len & highbit) != 0){
                    skip += 1;
                }
                int len2 = StringChunk[v + skip] & 0x0ff;
                skip += 1;
                int flen = len2;
                if((len2 & highbit) != 0){
                    int len3 = StringChunk[v + skip] & 0x0ff;
                    flen = ((len2 & ~highbit) << (8 * (isUtf8 ? 1 : 2))) | len3;
                    skip += 1;
                }
                byte[] ContentBytes = new byte[flen];
                System.arraycopy(StringChunk, v + skip, ContentBytes, 0, ContentBytes.length);
                return new String(ContentBytes, StandardCharsets.UTF_8);
            }else {
                int len = (ByteUtil.byte2ShortEx(StringChunk, v) & 0x0ffff);
                byte[] ContentBytes = new byte[len * 2];
                System.arraycopy(StringChunk, v + 2, ContentBytes, 0, ContentBytes.length);
                return new String(ContentBytes, StandardCharsets.UTF_16LE);
            }
        }).collect(Collectors.toList()));
    }

    public StringPoolImpl(byte[] src, boolean hasHead) {
        int size = ByteUtil.byte2intEx(src, hasHead ? 12 : 4);//鍖烘澶у皬
        byte[] pool = new byte[size];
        System.arraycopy(src,  hasHead ? 8 : 0, pool, 0, pool.length);
        mType = ByteUtil.byte2int(pool);
        int sCount = ByteUtil.byte2intEx(pool, 8);
        int iStyleCount = ByteUtil.byte2intEx(pool, 12);
        int mFlag = ByteUtil.byte2intEx(pool, 16);
        boolean isUtf8 = (mFlag & IS_UTF8) != 0;
        int iStrOffset = ByteUtil.byte2intEx(pool, 20);
        int iStyOffset = ByteUtil.byte2intEx(pool, 24);
        if(iStyOffset != 0){
            System.err.println("Find Style!!!");
        }
        for (int i = 0; i < iStyleCount; i++) {
            int id = ByteUtil.byte2intEx(pool, iStyOffset + i * 4);
            mStyleIds.add(id);
        }
        byte[] StringChunk = new byte[size];
        System.arraycopy(pool, iStrOffset, StringChunk, 0, size - iStrOffset);
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < sCount; i++) {
            int StringOffset = ByteUtil.byte2intEx(pool, 28 + 4 * i);
            integerList.add(StringOffset);
        }
        add(StringChunk, integerList, isUtf8);
    }

    public byte[] getStringBytes() {
        byte[] result = new byte[0];
        for (String s : mContents) {
            byte[] item = new byte[0];
            item = ByteUtil.byteConcatEx(item, ByteUtil.shortToByte((short) s.length()));
            item = ByteUtil.byteConcatEx(item, s.getBytes(StandardCharsets.UTF_16LE));
            item = ByteUtil.byteConcatEx(item, new byte[]{0, 0});
            result = ByteUtil.byteConcatEx(result, item);
        }
        if(result.length % 4 != 0){
            result = ByteUtil.byteConcatEx(result, new byte[4 - (result.length % 4)]);//鍥涘瓧鑺傚榻?
        }
        return result;
    }

    @Override
    public byte[] build() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        LittleEndianDataOutputStream stream = new LittleEndianDataOutputStream(outputStream);
        byte result[] = getBytes();
        header.mResSize = result.length + 8;
//        header.build(stream);
        stream.write(result);
        byte data[] = outputStream.toByteArray();
        byte[] head = ByteUtil.shortToByte((short) 28);
        assert head.length == 2;
        System.arraycopy(head, 0, data, 2, head.length);
//        StringPoolImpl i = new StringPoolImpl(data);
//        for (String s : i.mContents) {
//            System.out.println(s);
//        }
        return data;
    }

    @Override
    public byte[] getBytes() {
        byte[] str = getStringBytes();//String 鍐呭
        byte[] strOffsetList = new byte[0];
        int currentOffset = 0;
        for (String s : mContents) {
            strOffsetList = ByteUtil.byteConcatEx(strOffsetList, ByteUtil.int2Byte(currentOffset));
            currentOffset += 4 + s.getBytes(StandardCharsets.UTF_16LE).length;
        }
        byte[] styList = new byte[0];
        for (Integer mStyleId : mStyleIds) {
            styList = ByteUtil.byteConcatEx(styList, ByteUtil.int2Byte(mStyleId));
        }
        byte[] result = new byte[0];
        result = ByteUtil.byteConcatEx(result, ByteUtil.int2Byte(mType));//绫诲瀷
        result = ByteUtil.byteConcatEx(result, ByteUtil.int2Byte(str.length + 28 + strOffsetList.length + styList.length));//澶у皬
        result = ByteUtil.byteConcatEx(result, ByteUtil.int2Byte(mContents.size()));//瀛楃涓叉暟閲?
        result = ByteUtil.byteConcatEx(result, ByteUtil.int2Byte(mStyleIds.size()));//鏍峰紡鏁伴噺
        result = ByteUtil.byteConcatEx(result, new byte[4]);//Flag None
        result = ByteUtil.byteConcatEx(result, ByteUtil.int2Byte(strOffsetList.length + 28));//瀛楃涓插亸绉?
        result = ByteUtil.byteConcatEx(result, mStyleIds.size() == 0 ? new byte[4]: ByteUtil.int2Byte(28 + strOffsetList.length
        + str.length));//鏍峰紡鍋忕Щ
        result = ByteUtil.byteConcatEx(result, strOffsetList);//瀛楃涓插亸绉诲垪琛?
        result = ByteUtil.byteConcatEx(result, str);//瀛楃涓插疄浣?
        result = ByteUtil.byteConcatEx(result, styList);//鏍峰紡鍒楄〃
        return result;
    }

    @Override
    public int getStringCount() {
        return mContents.size();
    }

    @Override
    public int findStringId(String content) {
        return mContents.indexOf(content);
    }

    @Override
    public int getOrCreateString(String content) {
        int id = findStringId(content);
        if (id != -1) {
            return id;
        } else {
            mContents.add(content);
            return mContents.size() - 1;
        }
    }

    @Override
    public String getString(int id) {
        try {
            return mContents.get(id);
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    public void insertString(int pos, String content){
        mContents.add(pos, content);
    }

    @Override
    public void setStringById(int id, String content) {
        mContents.set(id, content);
    }

    @Override
    public String read(int id) {
        return getString(id);
    }
}

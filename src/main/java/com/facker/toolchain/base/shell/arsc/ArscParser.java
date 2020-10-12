package com.facker.toolchain.base.shell.arsc;

import com.google.common.annotations.Beta;
import com.google.common.io.LittleEndianDataInputStream;
import com.facker.toolchain.base.shell.axml.ByteUtil;
import com.facker.toolchain.base.shell.axml.struct.StringPoolImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Beta
public class ArscParser {
    private LittleEndianDataInputStream mInputStream;
    public StringPoolImpl mStringPool;
    public final ArscHeader header;
    public List<TablePackageType> tablePackages = new ArrayList<>();
    public List<BuildAble> buildAbles = new ArrayList<>();

    public ArscParser(File file) throws IOException {
        this.mInputStream = new LittleEndianDataInputStream(new BufferedInputStream(new FileInputStream(file)));
        header = new ArscHeader(mInputStream);
        int PKG_NUM = mInputStream.readInt();
        System.out.printf("Type:0x%04x %d, FileSize:%d PkgNum:%d\n",
                header.mType, header.mHeadSize, header.mResSize, PKG_NUM);
        while (mInputStream.available() > 0){
            ArscHeader header = new ArscHeader(mInputStream);
            System.out.println(header);
            switch (header.mType){//具体Case 参照ResType
                case 0x1:
                    mStringPool = new StringPoolImpl(mInputStream, header);
                    buildAbles.add(mStringPool);
                    break;
                case 0x200:
                    TablePackageType tablePackage = new TablePackageType(mInputStream, header);
                    buildAbles.add(tablePackage);
                    tablePackages.add(tablePackage);
                    break;
                default:
                    buildAbles.add(new DefaultArscHolder(mInputStream, header));
            }
        }
    }

    /**
     * 参数必须是一个小端码数据输出流
     * @param output
     */
    public void write(DataOutput output) throws IOException {
        byte result[] = null;
        for (BuildAble buildAble : buildAbles) {
            result = ByteUtil.byteConcatEx(result, buildAble.build());
        }
        output.writeShort(header.mType);
        output.writeShort(0xC);//Fixed Length
        output.writeInt(result.length + 0xC);//Fixed Length
        output.writeInt(tablePackages.size());
        output.write(result);
    }

}

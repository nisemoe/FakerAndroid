package com.facker.toolchain.base.shell.arsc;

import com.google.common.io.LittleEndianDataOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.IOException;

/**
 * 固定8字节的一个数据类
 */
public class ArscResStringPoolRef implements BuildAble {
    static int SIZE = 8;
    int size, //short
    data_type, //unsign byte
    data, //int
    res0;//short

    public ArscResStringPoolRef(int data_type, int data, int res0) {
        this.size = 8;
        this.data_type = data_type;
        this.data = data;
        this.res0 = res0;
    }

    public ArscResStringPoolRef(DataInput stream, StringPoolReader reader) throws IOException {
        size = stream.readUnsignedShort();
        assert size == 8;
        res0 = stream.readUnsignedByte();
        data_type = stream.readUnsignedByte();
        data = stream.readInt();
    }

    public int size(){
        return 8;
    }

    @Override
    public String toString() {
        return String.format("[PoolRef: 0x%mybatis-config.xml, data: 0x%08x]",
                data_type, data);
    }

    @Override
    public byte[] build() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        LittleEndianDataOutputStream stream = new LittleEndianDataOutputStream(outputStream);
        stream.writeShort(size);
        stream.writeByte(res0);
        stream.writeByte(data_type);
        stream.writeInt(data);
        byte result[] = outputStream.toByteArray();
//        assert result.length == 8;
        return result;
    }
}

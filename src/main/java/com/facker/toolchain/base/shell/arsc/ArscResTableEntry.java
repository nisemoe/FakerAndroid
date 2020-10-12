package com.facker.toolchain.base.shell.arsc;

import com.google.common.io.LittleEndianDataOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.IOException;

public class ArscResTableEntry implements BuildAble {

    static int FLAG_COMPLEX = 1;
    static int FLAG_PUBLIC = 2;
    static int FLAG_WEAK = 4;
    int size, index, resId;
    final int flags;
    public ArscComplex mArscComplex;
    public ArscResStringPoolRef mPoolRef;
    public String _cacheValue;

    public ArscResTableEntry(int index, int flags,
                 ArscComplex mArscComplex, ArscResStringPoolRef mPoolRef) {
        this.index = index;
        this.flags = flags;
        this.mArscComplex = mArscComplex;
        this.mPoolRef = mPoolRef;
    }

    public ArscResTableEntry(DataInput stream, int ResId, StringPoolReader reader) throws IOException {
        size = stream.readUnsignedShort();
        flags = stream.readUnsignedShort();
        index = stream.readInt();
        resId = ResId;
        _cacheValue = reader.read(index);
        if(isComplex()){
            mArscComplex = new ArscComplex(stream, reader);
        }else {
            mPoolRef = new ArscResStringPoolRef(stream, reader);
        }
    }

    public int size(){
        return (isComplex() ? mArscComplex.size() : mPoolRef.size()) + 8;
    }

    public boolean isComplex(){
        return (flags & FLAG_COMPLEX) != 0;
    }

    public boolean isWeak(){
        return (flags & FLAG_WEAK) != 0;
    }

    @Override
    public String toString() {
        return String.format("index:%s, size:%d, flags:%mybatis-config.xml, id:0x%08x %s",
                _cacheValue, size, flags, resId, isComplex() ?
                        mArscComplex.toString():
                mPoolRef.toString());
    }

    @Override
    public byte[] build() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        LittleEndianDataOutputStream stream = new LittleEndianDataOutputStream(outputStream);
//        2 + 2 + 4;
        byte child[] = isComplex() ? mArscComplex.build() : mPoolRef.build();
        stream.writeShort(isComplex() ? 16 : 8);
        stream.writeShort(flags);
        stream.writeInt(index);
        stream.write(child);
        byte result[] = outputStream.toByteArray();
        return result;
    }
}

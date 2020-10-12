package com.facker.toolchain.base.shell.arsc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ArscHeader implements BuildAble {
    public final short mType;
    public final short mHeadSize;
    public int mResSize;

    public ArscHeader(DataInput stream) throws IOException {
        mType = stream.readShort();
        mHeadSize = stream.readShort();
        mResSize = stream.readInt();
    }

    public ArscHeader(short mType, short mHeadSize, int mResSize) {
        this.mType = mType;
        this.mHeadSize = mHeadSize;
        this.mResSize = mResSize;
    }

    @Override
    public byte[] build() {
        throw new RuntimeException("Not Support");
    }

    public void build(DataOutput output) throws IOException {
        output.writeShort(mType);
        output.writeShort(mHeadSize);
        output.writeInt(mResSize);
    }

    @Override
    public String toString() {
        return String.format("ArscHeader type: %mybatis-config.xml, heads: %d ress: %d",
                mType, mHeadSize, mResSize);
    }
}
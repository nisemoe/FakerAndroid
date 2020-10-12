package com.facker.toolchain.base.shell.arsc;

import com.google.common.io.LittleEndianDataOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.IOException;

public class DefaultArscHolder implements BuildAble{
    public final ArscHeader header;
    public final byte[] stores;

    public DefaultArscHolder(DataInput input, ArscHeader header) throws IOException {
        this.header = header;
        stores = new byte[header.mResSize - 8];
        input.readFully(stores);
    }

    @Override
    public byte[] build() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        LittleEndianDataOutputStream stream = new LittleEndianDataOutputStream(outputStream);
        header.build(stream);
        stream.write(stores);
        return outputStream.toByteArray();
    }
}

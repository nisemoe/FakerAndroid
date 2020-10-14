package com.facker.toolchain.arsc;

import com.google.common.io.LittleEndianDataOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ArscComplex implements BuildAble {

    int pid;
    LinkedHashMap<Integer, ArscResStringPoolRef> items = new LinkedHashMap<>();

    public ArscComplex(DataInput stream, StringPoolReader reader) throws IOException {
        pid = stream.readInt();
        int count = stream.readInt();
        for (int i = 0; i < count; i++) {
            int id = stream.readInt();
            if(items.containsKey(id)){
                throw new RuntimeException("Data Lossing");
            }
            items.put(id, new ArscResStringPoolRef(stream, reader));
        }
//        System.out.printf(String.format("Complex Here %d\n", 8 + count * 12));
    }

    public int size(){
        return 12*items.size() + 8;
    }

    @Override
    public String toString() {
        return items.toString();
    }

    @Override
    public byte[] build() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        LittleEndianDataOutputStream stream = new LittleEndianDataOutputStream(outputStream);
        stream.writeInt(pid);
        stream.writeInt(items.size());
        for (Map.Entry<Integer, ArscResStringPoolRef> entry : items.entrySet()) {
            stream.writeInt(entry.getKey());
            stream.write(entry.getValue().build());
        }
        byte result[] = outputStream.toByteArray();
//        assert result.length == 8 + items.size() * 12;
//        System.out.printf(String.format("Complex Here %d\n", result.length));
        return result;
    }
}

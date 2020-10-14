package com.facker.toolchain.arsc;

import com.google.common.io.LittleEndianDataOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArscResTypeSpec extends ArscChunk {

    public final List<Integer> typespecEntries = new ArrayList<>();
    public final byte id, res0;
    public final int res1;
    public final StringPoolReader mReader;
    public final List<ArscTableType> children = new ArrayList<>();

    public ArscResTypeSpec(byte id, byte res0, int res1, StringPoolReader reader, ArscHeader header){
        super(header);
        this.id = id;
        this.res0 = res0;
        this.res1 = res1;
        this.mReader = reader;
    }

    public ArscResTypeSpec(DataInput in, ArscHeader header, StringPoolReader reader) throws IOException {
        super(header);
        mReader = reader;
        id = in.readByte();
        res0 = in.readByte();
        res1 = in.readUnsignedShort();
        int entryCount = in.readInt();
        typespecEntries.clear();
        typespecEntries.addAll(Stream.generate(() -> {
            try {
                return in.readInt();
            } catch (IOException e) {
                throw new RuntimeException(e.getCause());
            }
        })
        .limit(entryCount)
        .collect(Collectors.toList()));
    }

    public String getId() {
        return mReader.read(id - 1);
    }

    public void addChild(ArscTableType table){
        table.setTypeString(getId());
        children.add(table);
    }

    @Override
    public String toString() {
        return String.format("id:%s, res0:%d, res1:0x%08x, " +
                        "count:%d datas:%s," +
                        "\nchildren: %s", getId(),
                res0, res1, typespecEntries.size(),
                typespecEntries.stream()
                .map(v->String.format("0x%08x", v)).collect(Collectors.joining(", ")),
                children.toString());
    }

    @Override
    public byte[] build() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        LittleEndianDataOutputStream stream = new LittleEndianDataOutputStream(outputStream);
        mHeader.mResSize = 16 + typespecEntries.size() * 4;
        mHeader.build(stream);
        stream.writeByte(id);
        stream.writeByte(res0);
        stream.writeShort(res1);
        stream.writeInt(typespecEntries.size());
        for (Integer entry : typespecEntries) {
            stream.writeInt(entry);
        }
        for (ArscTableType child : this.children) {
            outputStream.write(child.build());
        }
        byte result[] = outputStream.toByteArray();
        return result;
    }
}

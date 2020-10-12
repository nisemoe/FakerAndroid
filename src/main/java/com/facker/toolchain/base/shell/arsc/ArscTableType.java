package com.facker.toolchain.base.shell.arsc;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ArscTableType extends ArscChunk {

    public final byte id, res0;
    public final short res1;
    public final ArscResTableConfig config;
    public LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
    public List<ArscResTableEntry> entries = new ArrayList<>();
    public final StringPoolReader mReader;
    public int mPackageId;

    public void setTypeString(String _typeString) {
        this._typeString = _typeString;
    }

    private String _typeString;

    public ArscTableType(int id, int res0, int res1, ArscResTableConfig config,
                         StringPoolReader reader, int pid, ArscHeader header){
        super(header);
        this.id = (byte) id;
        this.res0 = (byte) res0;
        this.res1 = (short) res1;
        this.config = config;
        mReader = reader;
        mPackageId = pid;
    }

    public ArscTableType(LittleEndianDataInputStream stream, ArscHeader h,
                         TablePackageType instance, StringPoolReader reader) throws IOException {
        super(h);
        mReader = reader;
        id = stream.readByte();
        res0 = stream.readByte();
        res1 = stream.readShort();
        int eCount = stream.readInt();
        int eCountStart = stream.readInt();//12
        mPackageId = instance.mPackageId;
        int now = (0xff000000 & (instance.mPackageId << 24))|id << 16;
        config = new ArscResTableConfig(stream);
        for (int i = 0; i < eCount; i++) {
            map.put(now & 0xffff0000 | i, stream.readInt());
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            int k = entry.getKey();
            int v = entry.getValue();
            if(v != -1){
                entries.add(new ArscResTableEntry(stream, k, reader));
            }
        }
        assert eCountStart == config.build().length + 20 + map.size() * 4;
    }

    public int nextInt(){
        return entries.stream()
                .map(ArscResTableEntry::size)
                .reduce((x, y) -> x + y)
                .orElse(0);
    }

    public int nextId(){
        return map.keySet().stream().max((x, y) -> x - y)
                .map(i -> i + 1)
                .orElse((0xff000000 & (mPackageId << 24) | (id << 16)));
    }

    public int put(ArscResTableEntry entry){
        int i = nextId();
        map.put(i, nextInt());
        if(entry != null){
            entries.add(entry);
        }
        return i;
    }

    @Override
    public byte[] build() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        LittleEndianDataOutputStream stream = new LittleEndianDataOutputStream(outputStream);
        byte config[] = this.config.build();
        int bodySize = entries.stream()
                .map(ArscResTableEntry::size)
                .reduce((x, y) -> x + y)
                .orElse(0);
        mHeader.mResSize = 20 + config.length + map.size() * 4 + bodySize;
        mHeader.build(stream);
        stream.writeByte(id);
        stream.writeByte(res0);
        stream.writeShort(res1);
        stream.writeInt(map.size());
        stream.writeInt(config.length + 20 + map.size() * 4);
        stream.write(config);
        for (Integer integer : map.values()) {
            stream.writeInt(integer);
        }
        for (ArscResTableEntry entry : entries) {
            stream.write(entry.build());
        }
        byte result[] = outputStream.toByteArray();
        assert mHeader.mResSize == result.length;
        return result;
    }

    @Override
    public String toString() {
        return "<" + _typeString +
                "\n map=" + map +
                "\n entries=" + entries +
                "/>";
    }
}

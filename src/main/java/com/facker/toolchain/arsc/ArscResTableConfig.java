package com.facker.toolchain.arsc;

import com.google.common.io.LittleEndianDataOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.IOException;

public class ArscResTableConfig implements BuildAble {
    public int size = 0, imsi = 0, locale = 0, screenType = 0, input = 0,
    screenSize = 0, version = 0;
    public Integer screenConfig = null, screenDp = null;
    public byte[] extraData;

    public ArscResTableConfig(DataInput stream) throws IOException {
        size = stream.readInt();
        imsi = stream.readInt();
        locale = stream.readInt();
        screenType = stream.readInt();
        input = stream.readInt();
        screenSize = stream.readInt();
        version = stream.readInt();
        if(size >= 32){
            screenConfig = stream.readInt();
            if(size >= 36){
                screenDp = stream.readInt();
                int paddingSize = size - 36;
                if(paddingSize > 0){
                    extraData = new byte[paddingSize];
                    stream.readFully(extraData);
                }
            }
        }
    }

    protected ArscResTableConfig(){}

    public static ArscResTableConfig createDefaultConfig(){
        ArscResTableConfig config = new ArscResTableConfig();
        config.screenConfig = 0;
        config.screenDp = 0;
        config.extraData = new byte[64 - config.size()];
        return config;
    }

    @Override
    public String toString() {
        return "ArscResTableConfig{" +
                "size=" + size +
                '}';
    }

    public int size(){
        int s = 28;
        if(screenConfig != null){
            s = 32;
            if(screenDp != null){
                s = 36;
                if(extraData != null){
                    s = 36 + extraData.length;
                }
            }
        }
        return s;
    }

    @Override
    public byte[] build() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        LittleEndianDataOutputStream stream = new LittleEndianDataOutputStream(outputStream);
        int s = size();
        stream.writeInt(s);
        stream.writeInt(imsi);
        stream.writeInt(locale);
        stream.writeInt(screenType);
        stream.writeInt(input);
        stream.writeInt(screenSize);
        stream.writeInt(version);
        if (s >= 32){
            stream.writeInt(screenConfig);
            if(s >= 36){
                stream.writeInt(screenDp);
                if(s > 36 && extraData != null){
                    stream.write(extraData);
                }
            }
        }
//        stream.writeInt(new_data.length + 4);
//        stream.write(new_data);
        byte result[] = outputStream.toByteArray();
        return result;
    }
}

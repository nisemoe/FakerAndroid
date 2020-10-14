package com.facker.toolchain.arsc;

public abstract class ArscChunk implements BuildAble {

    protected ArscHeader mHeader;

    ArscChunk(ArscHeader header){
        mHeader = header;
    }

}

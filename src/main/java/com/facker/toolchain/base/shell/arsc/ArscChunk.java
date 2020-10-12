package com.facker.toolchain.base.shell.arsc;

public abstract class ArscChunk implements BuildAble {

    protected ArscHeader mHeader;

    ArscChunk(ArscHeader header){
        mHeader = header;
    }

}

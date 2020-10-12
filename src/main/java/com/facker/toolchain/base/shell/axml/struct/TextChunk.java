package com.facker.toolchain.base.shell.axml.struct;

import com.facker.toolchain.base.shell.axml.ByteUtil;

public class TextChunk implements Chunk {
	
	public byte[] type = new byte[4];
	public byte[] size = new byte[4];
	public byte[] org;

	public static TextChunk createChunk(byte[] byteSrc, int offset){
		TextChunk chunk = new TextChunk();
		chunk.type = ByteUtil.copyByte(byteSrc, 0, 4);
		chunk.size = ByteUtil.copyByte(byteSrc,  4, 4);
		int size = ByteUtil.byte2int(chunk.size);
		chunk.org = new byte[size - 8];
		System.arraycopy(byteSrc, 8, chunk.org, 0, chunk.org.length);
		return chunk;
		
	}

	@Override
	public byte[] getChunkByte() {
		return ByteUtil.byteConcatEx(type, size, org);
	}
}

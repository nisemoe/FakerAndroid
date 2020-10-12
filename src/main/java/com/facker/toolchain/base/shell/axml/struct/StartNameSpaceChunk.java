package com.facker.toolchain.base.shell.axml.struct;


import com.facker.toolchain.base.shell.axml.ByteUtil;

public class StartNameSpaceChunk implements Chunk {

	public byte[] type = new byte[4];
	public byte[] size = new byte[4];
	public byte[] lineNumber = new byte[4];
	public byte[] unknown = new byte[4];
	public byte[] prefix = new byte[4];
	public byte[] uri = new byte[4];

	public static StartNameSpaceChunk createChunk(byte[] byteSrc){

		StartNameSpaceChunk chunk = new StartNameSpaceChunk();

		chunk.type = ByteUtil.copyByte(byteSrc, 0, 4);
		
		chunk.size = ByteUtil.copyByte(byteSrc, 4, 4);

		chunk.lineNumber = ByteUtil.copyByte(byteSrc, 8, 4);

		chunk.unknown = ByteUtil.copyByte(byteSrc, 12, 4);
		
		chunk.prefix = ByteUtil.copyByte(byteSrc, 16, 4);

		chunk.uri = ByteUtil.copyByte(byteSrc, 20, 4);

		return chunk;

	}

	@Override
	public byte[] getChunkByte() {
		return ByteUtil.byteConcatEx(type, size, lineNumber, unknown, prefix, uri);
	}
}

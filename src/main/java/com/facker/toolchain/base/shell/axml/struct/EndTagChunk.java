package com.facker.toolchain.base.shell.axml.struct;


import com.facker.toolchain.base.shell.axml.ByteUtil;

public class EndTagChunk implements Chunk{
	
	public byte[] type = new byte[4];
	public byte[] size = new byte[4];
	public byte[] lineNumber = new byte[4];
	public byte[] unknown = new byte[4];
	public byte[] uri = new byte[4];
	public byte[] name = new byte[4];
	
	public int offset;

	public String optName;

	public void makeOpt(StringPool pool){
		optName = pool.getString(ByteUtil.byte2int(name));
	}

	@Override
	public String toString() {
		return String.format("End: %s", optName == null ? "" : optName);
	}

	public EndTagChunk(){
		type = ByteUtil.int2Byte(ChunkTypeNumber.CHUNK_ENDTAG);
		size = ByteUtil.int2Byte(24);
		lineNumber = new byte[4];
		unknown = new byte[4];
		uri = ByteUtil.int2Byte(-1);		
	}
	
	public static EndTagChunk createChunk(int name){
		EndTagChunk chunk = new EndTagChunk();
		chunk.name = ByteUtil.int2Byte(name);
		return chunk;
	}
	
	public byte[] getChunkByte(){
		byte[] bytes = new byte[getLen()];
		bytes = ByteUtil.byteConcat(bytes, type, 0);
		bytes = ByteUtil.byteConcat(bytes, size, 4);
		bytes = ByteUtil.byteConcat(bytes, lineNumber, 8);
		bytes = ByteUtil.byteConcat(bytes, unknown, 12);
		bytes = ByteUtil.byteConcat(bytes, uri, 16);
		bytes = ByteUtil.byteConcat(bytes, name, 20);
		return bytes;
	}
	
	public int getLen(){
		return type.length + size.length + lineNumber.length + unknown.length + uri.length + name.length;
	}
	
	public static EndTagChunk createChunk(byte[] byteSrc, int offset){
		
		EndTagChunk chunk = new EndTagChunk();
		
		chunk.offset = offset;
		
		chunk.type = ByteUtil.copyByte(byteSrc, 0, 4);
		
		chunk.size = ByteUtil.copyByte(byteSrc, 4, 4);
		
		chunk.lineNumber = ByteUtil.copyByte(byteSrc, 8, 4);

		chunk.unknown = ByteUtil.copyByte(byteSrc, 12, 4);

		chunk.uri = ByteUtil.copyByte(byteSrc, 16, 4);

		chunk.name = ByteUtil.copyByte(byteSrc, 20, 4);
		
		return chunk;
	}

}

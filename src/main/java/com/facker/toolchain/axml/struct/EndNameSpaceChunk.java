package com.facker.toolchain.axml.struct;


import com.facker.toolchain.axml.ByteUtil;

public class EndNameSpaceChunk{
	
	public byte[] type = new byte[4];
	public byte[] size = new byte[4];
	public byte[] lineNumber = new byte[4];
	public byte[] unknown = new byte[4];
	public byte[] prefix = new byte[4];
	public byte[] uri = new byte[4];
	
	public static EndNameSpaceChunk createChunk(byte[] byteSrc){

		EndNameSpaceChunk chunk = new EndNameSpaceChunk();

		//����type
		chunk.type = ByteUtil.copyByte(byteSrc, 0, 4);
		
		//����size
		chunk.size = ByteUtil.copyByte(byteSrc, 4, 4);

		//�����к�
		chunk.lineNumber = ByteUtil.copyByte(byteSrc, 8, 4);

		//����unknown
		chunk.unknown = ByteUtil.copyByte(byteSrc, 12, 4);
		
		//����prefix(������Ҫע������кź�����ĸ��ֽ�ΪFFFF,����)
		chunk.prefix = ByteUtil.copyByte(byteSrc, 16, 4);

		//����Uri
		chunk.uri = ByteUtil.copyByte(byteSrc, 20, 4);

		return chunk;

	}

}

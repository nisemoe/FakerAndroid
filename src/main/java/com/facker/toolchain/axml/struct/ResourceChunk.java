package com.facker.toolchain.axml.struct;

import com.google.common.io.LittleEndianDataOutputStream;
import com.facker.toolchain.axml.ByteUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ResourceChunk {
	
	public byte[] type;
	public byte[] size;

	public ArrayList<Integer> resourcIdList;
	
	public static ResourceChunk createChunk(byte[] byteSrc, int offset){
		ResourceChunk chunk = new ResourceChunk();
		chunk.type = ByteUtil.copyByte(byteSrc, 0+offset, 4);
		chunk.size = ByteUtil.copyByte(byteSrc, 4+offset, 4);
		int chunkSize = ByteUtil.byte2int(chunk.size);

		byte[] resourceIdByte = ByteUtil.copyByte(byteSrc, 8+offset, chunkSize-8);
		ArrayList<Integer> resourceIdList = new ArrayList<Integer>(resourceIdByte.length/4);
		for(int i=0;i<resourceIdByte.length;i+=4){
			int resId = ByteUtil.byte2int(ByteUtil.copyByte(resourceIdByte, i, 4));
			resourceIdList.add(resId);
		}
		chunk.resourcIdList = resourceIdList;
		return chunk;
	}

	public boolean isRes(int i){
		return i >=0 && i < resourcIdList.size();
	}

	public int getResId(int index){
		return resourcIdList.get(index);
	}

	public byte[] save() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		LittleEndianDataOutputStream stream = new LittleEndianDataOutputStream(out);
		stream.write(type);
		stream.writeInt(resourcIdList.size() * 4 + 8);
		for (Integer i : resourcIdList) {
			stream.writeInt(i);
		}
		stream.flush();
		return out.toByteArray();
	}




	public int getSize() {
		return ByteUtil.byte2int(size);
	}

	public int getCount(){
		return getSize() / 4;
	}
}

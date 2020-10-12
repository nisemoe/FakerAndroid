package com.facker.toolchain.base.shell.axml;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ByteUtil {
	
	public static byte[] byteConcat(byte[] src, byte[] subB, int start){
		for(int i=0;i<subB.length;i++){
			src[i+start] = subB[i];
		}
		return src;
	}
	public static byte[] byteConcatEx(byte[]... data){
		byte result[] = new byte[0];
		for (byte[] d : data) {
			result = ByteUtil.byteConcatEx(result, d);
		}
		return result;
	}


	public static byte[] byteConcatEx(byte[] src, byte[] subB){
		return byteConcatEx(src, subB, 0);
	}

	public static byte[] byteConcatEx(byte[] src, byte[] subB, int offset){
		if(src == null){
			return subB;
		}
		byte[] dest = new byte[src.length + subB.length];
		System.arraycopy(src, 0, dest, 0, src.length);
		System.arraycopy(subB, 0, dest, src.length + offset, subB.length);
		return dest;
	}
	
	public static int byte2int(byte[] res) { 
		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00)
				| ((res[2] << 24) >>> 8) | (res[3] << 24); 
		return targets; 
	}

	/**
	 * 指定偏移处的数值转为Int (小端)
	 * @param res
	 * @param offset
	 * @return
	 */
	public static int byte2intEx(byte[] res, int offset) {
		int targets = (res[offset+0] & 0xff) | ((res[offset+1] << 8) & 0xff00)
				| ((res[offset+2] << 24) >>> 8) | (res[offset+3] << 24);
		return targets;
	}
	
	public static byte[] int2Byte(int value) {   
	    byte[] src = new byte[4];  
	    src[3] =  (byte) ((value>>24) & 0xFF);  
	    src[2] =  (byte) ((value>>16) & 0xFF);  
	    src[1] =  (byte) ((value>>8) & 0xFF);    
	    src[0] =  (byte) (value & 0xFF);                  
	    return src;   
	}  
	
    public static byte[] shortToByte(short number) { 
        int temp = number; 
        byte[] b = new byte[2]; 
        for (int i = 0; i < b.length; i++) { 
            b[i] = new Integer(temp & 0xff).byteValue();// 
            temp = temp >> 8; // 向右移8位 
        } 
        return b; 
    } 
	
    public static short byte2Short(byte[] b) { 
        short s = 0; 
        short s0 = (short) (b[0] & 0xff);
        short s1 = (short) (b[1] & 0xff); 
        s1 <<= 8; 
        s = (short) (s0 | s1); 
        return s; 
    }
    public static short byte2ShortEx(byte[] b, int offset) {
        short s = 0;
        short s0 = (short) (b[0 + offset] & 0xff);
        short s1 = (short) (b[1 + offset] & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }
	
	public static String bytesToHexString(byte[] src1){  
		byte[] src = reverseBytes(src1);
		StringBuilder stringBuilder = new StringBuilder("");  
		if (src == null || src.length <= 0) {  
			return null;  
		}  
		for (int i = 0; i < src.length; i++) {  
			int v = src[i] & 0xFF;  
			String hv = Integer.toHexString(v);  
			if (hv.length() < 2) {  
				stringBuilder.append(0);  
			}  
			stringBuilder.append(hv+" ");  
		}  
		return stringBuilder.toString();  
	}  
	
	public static char[] getChars(byte[] bytes) {
		Charset cs = Charset.forName ("UTF-8");
		ByteBuffer bb = ByteBuffer.allocate (bytes.length);
		bb.put (bytes);
		bb.flip ();
		CharBuffer cb = cs.decode (bb);
		return cb.array();
	}
	
	public static byte[] addByte(byte[] src, byte[] add){
		if(src == null){
			return null;
		}
		if(add == null){
			return src;
		}
		byte[] newsrc = new byte[src.length+add.length];
		for(int i=0;i<src.length;i++){
			newsrc[i] = src[i];
		}
		for(int i=src.length;i<newsrc.length;i++){
			newsrc[i] = add[i-src.length];
		}
		return newsrc;
	}

	/**
	 * 对现有数组插入新的数组，
	 * @param src
	 * @param content
	 * @param offset 这里的位置为src参数中的偏移，以0开始计算
	 * @return
	 */
	public static byte[] insertByteEx(byte[] src, byte[] content, int offset){
		byte[] newSrc = new byte[src.length + content.length];
		int copied = Math.min(src.length, offset);
		System.arraycopy(src, 0, newSrc, 0, copied);
		System.arraycopy(content, 0, newSrc, copied, content.length);
		System.arraycopy(src, copied, newSrc, content.length + copied, src.length - copied);
		return newSrc;
	}

	public static byte[] insertByte(byte[] src, byte[] content, int offset){
		return insertByte(src, offset, content);
	}
	public static byte[] insertByte(byte[] src, int start, byte[] insertB){
		if(src == null){
			return null;
		}
		if(start > src.length){
			return null;
		}
		byte[] newB = new byte[src.length + insertB.length];
		for(int i = 0 ;i < start; i++){
			newB[i] = src[i];
		}
		for(int i=0;i<insertB.length;i++){
			newB[i+start] = insertB[i]; 
		}
		for(int i=start;i<src.length;i++){
			newB[i+insertB.length] = src[i];
		}
		return newB;
	}
	
	public static byte[] removeByte(byte[] src, int start, int len){
		if(src == null){
			return null;
		}
		if(start > src.length){
			return null;
		}
		if((start+len) > src.length){
			return null;
		}
		if(start<0){
			return null;
		}
		if(len<=0){
			return null;
		}
		byte[] dest = new byte[src.length-len];
		for(int i=0;i<=start;i++){
			dest[i] = src[i];
		}
		int k = 0;
		for(int i=(start+len);i<src.length;i++){
			dest[start+k] = src[i];
			k++;
		}
		return dest;
	}

	public static byte[] copyByte(byte[] src, int start, int len){
		if(src == null){
			return null;
		}
		if(start > src.length){
			return null;
		}
		if((start+len) > src.length){
			return null;
		}
		if(start<0){
			return null;
		}
		if(len<=0){
			return null;
		}
		byte[] resultByte = new byte[len];
		for(int i=0;i<len;i++){
			resultByte[i] = src[i+start];
		}
		return resultByte;
	}
	public static int copyByteToInt(byte[] src, int start, int len){
		if(src == null){
			return 0;
		}
		if(start > src.length){
			return 0;
		}
		if((start+len) > src.length){
			return 0;
		}
		if(start<0){
			return 0;
		}
		if(len<=0){
			return 0;
		}
		byte[] resultByte = new byte[len];
		for(int i=0;i<len;i++){
			resultByte[i] = src[i+start];
		}
		return byte2int(resultByte);
	}

	public static byte[] replaceBytes(byte[] src, byte[] bytes, int start){
		if(src == null){
			return null;
		}
		if(bytes == null){
			return src;
		}
		if(start > src.length){
			return src;
		}
		if((start+bytes.length) > src.length){
			return src;
		}
		byte[] replaceB = new byte[bytes.length];
		for(int i=0;i<bytes.length;i++){
			replaceB[i] = src[i+start];
			src[i+start] = bytes[i];
		}
		return src;
	}

	/**
	 * 替换当前现有对象的字节
	 * @param src
	 * @param dest
	 * @param offset
	 * @return
	 */
	public static byte[] replaceBytesEx(byte[] src, byte[] dest, int offset){
		System.arraycopy(dest, 0, src, offset, dest.length);
		return src;
	}
	
	public static byte[] reverseBytes(byte[] bytess){
		byte[] bytes = new byte[bytess.length];
		for(int i=0;i<bytess.length;i++){
			bytes[i] = bytess[i];
		}
    	if(bytes == null || (bytes.length % 2) != 0){
    		return bytes;
    	}
    	int i = 0, len = bytes.length;
    	while(i < (len/2)){
    		byte tmp = bytes[i];
    		bytes[i] = bytes[len-i-1];
    		bytes[len-i-1] = tmp;
    		i++;
    	}
    	return bytes;
    }
	
	public static String filterStringNull(String str){
		if(str == null || str.length() == 0){
			return str;
		}
		byte[] strByte = str.getBytes();
		ArrayList<Byte> newByte = new ArrayList<Byte>();
		for(int i=0;i<strByte.length;i++){
			if(strByte[i] != 0){
				newByte.add(strByte[i]);
			}
		}
		byte[] newByteAry = new byte[newByte.size()];
		for(int i=0;i<newByteAry.length;i++){
			newByteAry[i] = newByte.get(i);
		}
		return new String(newByteAry);
	}
	
	public static String getStringFromByteAry(byte[] srcByte, int start){
		if(srcByte == null){
			return "";
		}
		if(start < 0){
			return "";
		}
		if(start >= srcByte.length){
			return "";
		}
		byte val = srcByte[start];
		int i = 1;
		ArrayList<Byte> byteList = new ArrayList<Byte>();
		while(val != 0){
			byteList.add(srcByte[start+i]);
			val = srcByte[start+i];
			i++;
		}
		byte[] valAry = new byte[byteList.size()];
		for(int j=0;j<byteList.size();j++){
			valAry[j] = byteList.get(j); 
		}
		try{
			return new String(valAry, "UTF-8");
		}catch(Exception e){
			System.out.println("encode error:"+e.toString());
			return "";
		}
	}

}

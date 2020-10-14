package com.facker.toolchain.axml.struct;


import com.facker.toolchain.axml.ByteUtil;

public class AttributeData {
	
	public int nameSpaceUri;
	public int name;
	public int valueString;
	public int type = 0;
	public int data = 0;

	public String optName, optValue;


	public void makeOpt(StringPool pool){
		optName = pool.getString(name);
		optValue = pool.getString(valueString);
	}

	@Override
	public String toString() {
		if(optName == null){
			return String.format("key:%d,value: %d, %d", name, valueString, data);
		}else {
			return String.format("key:%s, value: %s", optName, optValue);
		}
	}

	public int offset;
	
	public int getLen(){
		return 20;
	}
	
	public static AttributeData createAttribute(byte[] src){
		AttributeData data = new AttributeData();
		data.nameSpaceUri = ByteUtil.byte2intEx(src, 0);
		data.name = ByteUtil.byte2intEx(src, 4);
		data.valueString = ByteUtil.byte2intEx(src, 8);
		data.type = ByteUtil.byte2intEx(src, 12);
		data.data = ByteUtil.byte2intEx(src, 16);
		return data;
	}
	
	public byte[] getByte(){
//		byte[] bytes = new byte[20];
		return ByteUtil.byteConcatEx(ByteUtil.int2Byte(nameSpaceUri),
				ByteUtil.int2Byte(name), ByteUtil.int2Byte(valueString),
				ByteUtil.int2Byte(type), ByteUtil.int2Byte(data));
	}

	/**
	 * 创建一个属性数据
	 * @param uri uri id
	 * @param name name id
	 * @param value value id
	 * @param type 类型
	 * @param data1 数据
	 * @return
	 */
	public static AttributeData createAttribute(int uri, int name, int value, int type, int data1){
		AttributeData data = new AttributeData();
		data.nameSpaceUri = uri;
		data.name = name;
		data.valueString = value;
		data.type = type;
		data.data = data1;
		return data;
	}
}

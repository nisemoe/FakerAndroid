package com.facker.toolchain.base.shell.axml;

import com.google.common.io.LittleEndianDataOutputStream;
import com.facker.toolchain.base.utils.IOUtil;
import com.facker.toolchain.base.shell.axml.struct.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BinXmlParser {

    byte[] xmlSrc;
    private int mFlag;
    private byte[] magicNumber;
    private StringPoolImpl mStringChunk;
    private ResourceChunk resChunk;
    private StartNameSpaceChunk startNamespaceChunk;
    private EndNameSpaceChunk endNamespaceChunk;
    private ArrayList<Chunk> mChunks = new ArrayList<>();
    public static final int FILE_HEAD = 8;
    public static String C_URI = "http://schemas.android.com/apk/res/android";
    public final static List<Integer> Types = Arrays.asList(ChunkTypeNumber.CHUNK_ENDTAG, ChunkTypeNumber.CHUNK_STARTTAG,
            ChunkTypeNumber.CHUNK_ENDNS, ChunkTypeNumber.CHUNK_STARTNS, ChunkTypeNumber.CHUNK_TEXT,
            ChunkTypeNumber.CHUNK_HEAD, ChunkTypeNumber.CHUNK_STRING);

    public static final String KEY_TAG_NAME = "__TAG_NAME__";
    public static final String KEY_SUB_TAG = "__SUB_TAG__";
    private final boolean mDebugModel = false;

//    private static Map<String, Integer> map = new HashMap<>();
//
//    static {
//        map.putIfAbsent("authorities", 0x1010018);
//        map.putIfAbsent("grantUriPermissions", 0x0101001b);
//        map.putIfAbsent("debuggable", 0x0101000f);
//        map.putIfAbsent("networkSecurityConfig", 0x01010527);
//        map.putIfAbsent("appComponentFactory", 0x0101057a);
//        map.put("isGame", 0x10103f4);
//        map.put("theme", 0x01010000);
//        map.put("label", 0x01010001);
//        map.put("icon", 0x01010002);
//        map.put("name", 0x01010003);
//        map.put("enabled", 0x0101000e);
//    }

    private AttributeType mType;

    public BinXmlParser(byte[] src) {
        newBytesAreaCreate(src);
    }

    public BinXmlParser(File src) throws IOException {
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(src));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtil.copy(inputStream, outputStream);
        newBytesAreaCreate(outputStream.toByteArray());
    }


    public List<AttributeData> memListAttr(String tagName) {
        for (Chunk ichunk : mChunks) {
            if (ichunk instanceof StartTagChunk) {
                StartTagChunk chunk = (StartTagChunk) ichunk;
                int tagNameIndex = chunk.name;
                String tagNameTmp = mStringChunk.getString(tagNameIndex);
                if (tagName.equals(tagNameTmp)) {
                    return chunk.attrList;
                }
            }
        }
        return new ArrayList<>();
    }

    public String memReadAttr(String tag, String tagName, String attrName) {
        AttributeType Type = new AttributeType(mStringChunk);
        for (Chunk ichunk : mChunks) {
            if (ichunk instanceof StartTagChunk) {
                final StartTagChunk chunk = (StartTagChunk) ichunk;
                int tagNameIndex = chunk.name;
                String tagNameTmp = mStringChunk.getString(tagNameIndex);

                if (tag.equals(tagNameTmp)) {
                    if (tag.equals("application") || tag.equals("manifest") || tag.equals("uses-sdk")) {
                        for (AttributeData data : chunk.attrList) {
                            String attrNameTemp1 = mStringChunk.getString(data.name);
                            if (attrName.equals(attrNameTemp1)) {
                                return Type.getAttributeData(data);
                            }
                        }
                    }
                    for (AttributeData attrData : chunk.attrList) {
                        String attrNameTemp = mStringChunk.getString(attrData.name);
                        if ("name".equals(attrNameTemp)) {
                            String value = mStringChunk.getString(attrData.valueString);
                            if (tagName.equals(value)) {
                                for (AttributeData data : chunk.attrList) {
                                    String attrNameTemp1 = mStringChunk.getString(data.name);
                                    if (attrName.equals(attrNameTemp1)) {
                                        return Type.getAttributeData(data);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public void clear() {
        xmlSrc = null;
        mFlag = 0;
        magicNumber = null;
        mStringChunk = null;
        resChunk = null;
        startNamespaceChunk = null;
        endNamespaceChunk = null;
        mChunks.clear();
    }

    public void resDump() {
        for (int i = 0; i < mStringChunk.getStringCount(); i++) {
            System.out.printf("%d:%s, 0x%mybatis-config.xml\n", i,
                    mStringChunk.getString(i),
                    i >= resChunk.resourcIdList.size() ? 0 :
                            resChunk.resourcIdList.get(i));
        }
    }

    public void newBytesAreaCreate(byte[] bytes) {
        clear();
        this.xmlSrc = bytes;
        this.magicNumber = ByteUtil.copyByte(xmlSrc, 0, 4);
        try {
            mStringChunk = new StringPoolImpl(xmlSrc);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("System Can't Find Decoder");
        }

        mType = new AttributeType(mStringChunk);
        int resOffset = FILE_HEAD + ByteUtil.byte2intEx(xmlSrc, 12);
        resChunk = ResourceChunk.createChunk(xmlSrc, resOffset);
        int xmlChunkOffset = resOffset + resChunk.getSize();
        int _offset = xmlChunkOffset;

        while (_offset < xmlSrc.length) {
            int chunkTag = ByteUtil.copyByteToInt(xmlSrc, _offset, 4);
            int chunkSize = ByteUtil.copyByteToInt(xmlSrc, _offset + 4, 4);
            byte[] _current = ByteUtil.copyByte(xmlSrc, _offset, chunkSize);
            switch (chunkTag) {
                case ChunkTypeNumber.CHUNK_STARTNS:
                    startNamespaceChunk = StartNameSpaceChunk.createChunk(ByteUtil.copyByte(xmlSrc, _offset, chunkSize));
                    mChunks.add(startNamespaceChunk);
                    break;
                case ChunkTypeNumber.CHUNK_STARTTAG:
                    StartTagChunk _tagChunk = StartTagChunk.createChunk(_current, _offset);
                    _tagChunk.makeOpt(mStringChunk);
                    mChunks.add(_tagChunk);
                    break;
                case ChunkTypeNumber.CHUNK_ENDTAG:
                    EndTagChunk endTagChunk = EndTagChunk.createChunk(_current, _offset);
                    endTagChunk.makeOpt(mStringChunk);
                    mChunks.add(endTagChunk);
                    break;
                case ChunkTypeNumber.CHUNK_ENDNS:
                    endNamespaceChunk = EndNameSpaceChunk.createChunk(_current);
                    mChunks.add(StartNameSpaceChunk.createChunk(_current));
                    break;
                case ChunkTypeNumber.CHUNK_TEXT:
                    mChunks.add(TextChunk.createChunk(_current, _offset));
                    break;
                default:
                    _offset += 4;
                    System.err.println(String.format("unknow Tag 0x%mybatis-config.xml abort", chunkTag));
            }
            _offset += chunkSize;
        }
    }

    protected List<Chunk> createChunks(List<Map<String, Object>> data) {
        final List<Chunk> chunks = new ArrayList<>();
        for (Map<String, Object> d : data) {
            chunks.addAll(createChunks(d));
        }
        return chunks;
    }

    protected List<Chunk> createChunks(Map<String, ? extends Object> map) {
        String tagName = null;
        List<Chunk> subTag = new ArrayList<>();
        List<Chunk> result = new ArrayList<>();
        List<AttributeData> attributeData = new ArrayList<>();
        for (Map.Entry<String, ? extends Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object obj = entry.getValue();
            if (KEY_TAG_NAME.equals(key)) {
                tagName = (String) obj;
            } else if (KEY_SUB_TAG.equals(key)) {
                subTag.addAll(createChunks((List<Map<String, Object>>) obj));
            } else {
                attributeData.add(createAttributeData(key, (String) obj));
            }
        }
        if (tagName == null || tagName.isEmpty()) {
            throw new RuntimeException("TagName Not Set");
        }
        int tagNameId = getOrCreateString(tagName);
        StartTagChunk chunkStart = StartTagChunk.createChunk(tagNameId, -1, attributeData);
        EndTagChunk chunkEnd = EndTagChunk.createChunk(tagNameId);
        chunkEnd.makeOpt(mStringChunk);
        result.add(chunkStart);
        result.addAll(subTag);
        result.add(chunkEnd);
        return result;
    }

    public void insertChunks(List<Chunk> chunks) {
        insertChunks(chunks, false);
    }

    public void insertChunks(List<Chunk> chunks, boolean inApplication) {
        int pos = -1;

        for (int i = 0; i < mChunks.size(); i++) {
            if (mChunks.get(i) instanceof StartTagChunk) {
                StartTagChunk startTagChunk = (StartTagChunk) mChunks.get(i);
                final String name = getString(startTagChunk.name);
                if ((inApplication ? "application" : "manifest").equalsIgnoreCase(name)) {
                    pos = i;
                    break;
                }
            }
        }
        if (pos != -1) {
            for (Chunk chunk : chunks) {
                if (chunk instanceof StartTagChunk) {
                    ((StartTagChunk) chunk).makeOpt(mStringChunk);
                }
            }
            mChunks.addAll(pos + 1, chunks);
        }
    }

    /**
     *
     * @param tagName 标签名称
     * @param key 在xml明文中name对应的值
     * @param nameValue 欲删除的属性名
     * @param data 字段为null时表示删除属性
     */
    public void memModifyAttr(String tagName, String key, String nameValue, AttributeData data) {
        for (Chunk ichunk : mChunks) {
            if (ichunk instanceof StartTagChunk) {
                final StartTagChunk chunk = (StartTagChunk) ichunk;
                String tName = mStringChunk.getString(chunk.name);
                if (tName.equalsIgnoreCase(tagName)) {
                    boolean finded = false;
                    if (!(tagName.equals("application") || tagName.equals("manifest"))) {
                        for (AttributeData attributeData : chunk.attrList) {
                            String k = mStringChunk.getString(attributeData.name);
                            String v = mStringChunk.getString(attributeData.valueString);
                            if (k.equals("name") && v.equals(key)) {
                                finded = true;
                                break;
                            }
                        }
                    } else {
                        finded = true;
                    }
                    if (finded) {
                        for (int i = 0; i < chunk.attrList.size(); i++) {
                            String k = mStringChunk.getString(chunk.attrList.get(i).name);
                            if (k.equals(nameValue)) {
                                if(data == null){
                                    chunk.attrList.remove(i);
                                }else {
                                    chunk.attrList.set(i, data);
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 暂时只支持Application标签和Manifest标签
     * @param tag
     * @param data
     * @param pos 位置为-1时表示末尾
     */
    public void memAddAttr(String tag, AttributeData data, int pos) {
        for (Chunk ichunk : mChunks) {
            if (ichunk instanceof StartTagChunk) {
                final StartTagChunk chunk = (StartTagChunk) ichunk;
                String currentName = mStringChunk.getString(chunk.name);
                if (tag.equals(currentName)) {
                    if (currentName.equalsIgnoreCase("application") || currentName.equalsIgnoreCase("manifest")) {
                        int attrOffset = pos;
                        if (pos == -1) {
                            attrOffset = chunk.attrList.size();
                        }
                        for (Chunk mChunk : mChunks) {
                            if (mChunk == chunk) {
                                System.out.println("属性已插入");
                                ((StartTagChunk) mChunk).attrList.add(attrOffset, data);
                            }
                        }
                        return;
                    }
                }
            }
        }
    }

    public AttributeData createAttributeData(String key, int type, int data) {
        int attrName = memGetKeyString(key);
        int attrValue = -1;
        int attrUri = getOrCreateString(C_URI);
        type = ByteUtil.byte2int(ByteUtil.reverseBytes(ByteUtil.int2Byte(type)));
        return AttributeData.createAttribute(attrUri, attrName, attrValue, type, data);
    }

    public AttributeData createAttributeData(String key, String value) {
        int[] type = getAttrType(value);
        int attrName = memGetKeyString(key);
        int attrValue = isStringValue(value) ? getOrCreateString(value) : -1;
        int attrUri = getOrCreateString(C_URI);
        int attrType = type[0];
        int attrData = type[1];
        return AttributeData.createAttribute(attrUri, attrName, attrValue, attrType, attrData);
    }

    public boolean memRemoveTag(String tag, String name, boolean removeSelf){
        boolean start = false;
        int tagid = mStringChunk.findStringId(tag), keyid = mStringChunk.findStringId(name)
                ,nameid = mStringChunk.findStringId("name");
        if(tagid == -1 || keyid == -1 || nameid == -1){
            System.err.println("Not Valid Params For memRemoveTag");
            return false;
        }
        AtomicInteger depth = new AtomicInteger();
        for (int i = 0; i < mChunks.size(); i++) {
            if (mChunks.get(i) instanceof StartTagChunk){
                StartTagChunk chunk = (StartTagChunk) mChunks.get(i);
                if(!start){
                    if(tagid == chunk.name){
                        for (AttributeData data : chunk.attrList) {
                            if (data.name == nameid
                                    && data.valueString == keyid){
                                start = true;
                                depth.incrementAndGet();
                                break;
                            }
                        }
                        if (removeSelf && start){
                            mChunks.remove(i);
                            i--;
                        }
                    }
                }else {
                    mChunks.remove(i);
                    i--;
                    depth.incrementAndGet();
                }
            }else if(mChunks.get(i) instanceof EndTagChunk && start){
                if((removeSelf ? depth.getAndDecrement() : depth.decrementAndGet()) > 0){
                    mChunks.remove(i);
                    i--;
                }
                if(depth.get() == 0){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 查找启动Act
     *
     * @return 找到返回String，没有找到时返回null
     */
    public String findLauncherActivity() {
        final List<String> flag = Arrays.asList("intent-filter", "action", "category");
        final List<String> nav = Arrays.asList("provider", "receiver", "service", "uses-library");
        Map<String, String> chunks = new HashMap<>();
        String currentActivity = null;
        boolean inActivity = false;
        for (Chunk ichunk : mChunks) {
            if (ichunk instanceof StartTagChunk) {
                StartTagChunk chunk = (StartTagChunk) ichunk;
                String tagName = mStringChunk.getString(chunk.name);
                if (tagName.equalsIgnoreCase("activity") || tagName.equalsIgnoreCase("activity-alias")) {
                    inActivity = true;
                    currentActivity = tagName;
                    for (AttributeData data : chunk.attrList) {//获取当前act name
                        if (mStringChunk.getString(data.name).equals("name")) {
                            currentActivity = getString(data.valueString);
                        }
                    }
                } else if (flag.contains(tagName) && inActivity) {
                    if (tagName.equalsIgnoreCase("intent-filter")) {
                        chunks.clear();
                    }
                    String value = null;
                    for (AttributeData data : chunk.attrList) {
                        if (mStringChunk.getString(data.name).equals("name")) {
                            value = getString(data.valueString);
                        }
                    }
                    if (value == null) {
                        continue;
                    } else {
                        chunks.put(tagName, value);
                        boolean hasA = false, hasB = false;
                        for (Map.Entry<String, String> entry : chunks.entrySet()) {
                            if (entry.getKey().equals("action") && entry.getValue().equals("android.intent.action.MAIN")) {
                                hasA = true;
                            } else if (entry.getKey().equals("category") && entry.getValue().equals("android.intent.category.LAUNCHER")) {
                                hasB = true;
                            }
                        }
                        if (hasA && hasB) {
                            return currentActivity;
                        }
                    }
                } else if (nav.contains(tagName)) {
                    currentActivity = null;
                    inActivity = false;
                }
            }
        }
        return null;
    }

    /**
     *
     * @param targetTag
     * @param attrName
     * @param key 标签不唯一时使用标签的name属性删除
     * @return
     */
    public boolean memRemoveAttr(String targetTag, String attrName, String key) {
        int nameid = mStringChunk.findStringId("name");
        int attrid = mStringChunk.findStringId(attrName);
        int keyid = mStringChunk.findStringId(key);
        int tagid = mStringChunk.findStringId(targetTag);
        boolean single = targetTag.equalsIgnoreCase("application") ||
                targetTag.equalsIgnoreCase("manifest");
        for (Chunk chunk : mChunks) {
            if (chunk instanceof StartTagChunk) {
                StartTagChunk startTagChunk = (StartTagChunk) chunk;
                if (tagid == startTagChunk.name && single) {
                    for (int i = 0; i < startTagChunk.attrList.size(); i++) {
                        if (startTagChunk.attrList.get(i).name == attrid ) {
                            startTagChunk.attrList.remove(i);
                            System.out.println("已删除指定节点");
                            return true;
                        }
                    }
                }else if(tagid == startTagChunk.name && (!single)){
                    boolean find = false;
                    for (int i = 0; i < startTagChunk.attrList.size(); i++) {
                        if (startTagChunk.attrList.get(i).name == nameid &&
                                startTagChunk.attrList.get(i).valueString == keyid ) {
                            find = true;
                            break;
                        }
                    }
                    if (find){
                        for (AttributeData data : startTagChunk.attrList) {
                            if(data.name == attrid){
                                startTagChunk.attrList.remove(data);
                                System.out.println("已删除指定节点");
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    public int memGetKeyString(String key) {
        if (DefaultIdHolder.getInstance().strMap.containsKey(key)
                && mStringChunk.findStringId(key) == -1) {
            if (mStringChunk.getStringCount() > resChunk.resourcIdList.size()) {
                final String orgString = mStringChunk.getString(resChunk.resourcIdList.size());
                final int oldPos = resChunk.resourcIdList.size();
                mStringChunk.setStringById(oldPos, key);
                final int newPos = mStringChunk.getOrCreateString(orgString);
//                resChunk.resourcIdList.add(map.get(key));
                resChunk.resourcIdList.add(DefaultIdHolder.getInstance().strMap.get(key));
//                System.out.printf("原有Str: %s, Pos: %d\n", orgString, resChunk.resourcIdList.size());
                for (Chunk mChunk : mChunks) {
                    if (mChunk instanceof StartTagChunk) {
                        final int name = ((StartTagChunk) mChunk).name;
                        final String sname = getString(name);
                        if (name == oldPos) {
                            ((StartTagChunk) mChunk).name = newPos;
                            System.out.println("Check1");
                        }
                        for (AttributeData data : ((StartTagChunk) mChunk).attrList) {
                            if (!(data.name < 0) && data.name == oldPos) {
                                data.name = newPos;
                                System.out.println("Check2");
                            }
                            if (!(data.valueString < 0) && data.valueString == oldPos) {
                                data.valueString = newPos;
                                System.out.println("Check3");
                            }
                            if (!(data.nameSpaceUri < 0) && data.nameSpaceUri == oldPos) {
                                data.nameSpaceUri = newPos;
                                System.out.println("Check4");
                            }
                        }
                    } else if (mChunk instanceof EndTagChunk) {
                        final int name = ByteUtil.byte2int(((EndTagChunk) mChunk).name);
                        final String sname = getString(name);
                        if (sname.equals(orgString)) {
                            ((EndTagChunk) mChunk).name = ByteUtil.int2Byte(newPos);
                            System.out.println("Check5");
                        }
                    } else if (mChunk instanceof StartNameSpaceChunk) {
                        int pref = ByteUtil.byte2int(((StartNameSpaceChunk) mChunk).prefix);
                        int uri = ByteUtil.byte2int(((StartNameSpaceChunk) mChunk).uri);
                        if (pref == oldPos) {
                            ((StartNameSpaceChunk) mChunk).prefix = ByteUtil.int2Byte(newPos);
                            System.out.println("Check6");
                        }
                        if (uri == oldPos) {
                            ((StartNameSpaceChunk) mChunk).uri = ByteUtil.int2Byte(newPos);
                            System.out.println("Check7");
                        }
                    } else if (mChunk instanceof TextChunk) {
                        System.out.println("Do Nothing");
                    } else {
                        System.out.println("Unknow Type : " + mChunk);
                    }
                }
                return oldPos;
            } else {
                if(mStringChunk.getStringCount() == resChunk.resourcIdList.size()){
//                    resChunk.resourcIdList.add(map.get(key));
                    resChunk.resourcIdList.add(DefaultIdHolder.getInstance().strMap.get(key));
                    return mStringChunk.getOrCreateString(key);
                }
                throw new RuntimeException("The ResId Count Should Be Never Greater String Count.");
            }
        } else {
            return getOrCreateString(key);
        }
    }

    public boolean validBody(byte[] body){
        int current = 0;
        boolean debug = true;
        int index = 0;
        while (current < body.length){
            int type = ByteUtil.byte2intEx(body, current);
            int size = ByteUtil.byte2intEx(body, current + 4);
            current += size;
            if(size == 0 || !Types.contains(type)){
                System.out.printf("0x%08x 0x%08x\n", size, type);
                return false;
            }
            if(mDebugModel){
                System.out.printf("index:%d Type:0x%08x Size:0x%d\n", index, type, size);
                index ++;
            }
        }
        return true;
    }

    public byte[] memSave() {
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final LittleEndianDataOutputStream outputStream = new LittleEndianDataOutputStream(out);
            byte body[] = new byte[0], resMap[], strPool[];
            strPool = mStringChunk.getBytes();
            resMap = resChunk.save();
            System.out.printf("ResChunkSize: %d, %d\n", ByteUtil.byte2intEx(resMap, 4), resMap.length);
            System.out.printf("StrChunkSize: %d, %d\n", ByteUtil.byte2intEx(strPool, 4), strPool.length);
            outputStream.write(magicNumber);
            for (Chunk chunk : mChunks) {
                byte ready[] = chunk.getChunkByte();
                if(mDebugModel){
                    System.out.printf("Name:%s Type:%mybatis-config.xml Size:%d RealSize:%d\n",
                            chunk.getClass().getName(),
                            ByteUtil.byte2intEx(ready, 0),
                            ByteUtil.byte2intEx(ready, 4),
                            ready.length);
                }
                if(!Types.contains(ByteUtil.byte2int(ready)) || ready.length != ByteUtil.byte2intEx(ready, 4)){
                    System.out.println(String.format("%s has Error", chunk.toString()));
                }
                body = ByteUtil.byteConcatEx(body, ready);
            }
            System.out.println(validBody(body) ? "Xml Body区域验证通过" : "Xml Body 区域验证失败");
            System.out.printf("BodyChunkSize: %d\n", body.length);
            outputStream.writeInt(strPool.length + resMap.length + body.length + 8);//文件大小
            outputStream.write(strPool);
            outputStream.write(resMap);
            outputStream.write(body);
            outputStream.flush();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int[] getAttrType(String tagValue) {
        int[] result = new int[2];
        if (tagValue.equals("true") || tagValue.equals("false")) {//boolean
            result[0] = result[0] | AttributeType.ATTR_BOOLEAN;
            if (tagValue.equals("true")) {
                result[1] = 1;
            } else {
                result[1] = 0;
            }
        } else if (tagValue.equals("singleTask") || tagValue.equals("standard")
                || tagValue.equals("singleTop") || tagValue.equals("singleInstance")) {//启动模式int类型
            result[0] = result[0] | AttributeType.ATTR_FIRSTINT;
            if (tagValue.equals("standard")) {
                result[1] = 0;
            } else if (tagValue.equals("singleTop")) {
                result[1] = 1;
            } else if (tagValue.equals("singleTask")) {
                result[1] = 2;
            } else {
                result[1] = 3;
            }
        } else if (tagValue.equals("minSdkVersion") || tagValue.equals("versionCode")) {
            result[0] = result[0] | AttributeType.ATTR_FIRSTINT;
            result[1] = Integer.valueOf(tagValue);
        } else if (tagValue.startsWith("@")) {//引用
            result[0] = result[0] | AttributeType.ATTR_REFERENCE;
            result[1] = Integer.valueOf(tagValue.substring(1), 16);
        } else if (tagValue.startsWith("#")) {//色值
            result[0] = result[0] | AttributeType.ATTR_ARGB4;
            result[1] = Integer.valueOf(tagValue.substring(1), 16);
        } else {//字符串
            result[0] = result[0] | AttributeType.ATTR_STRING;
            result[1] = getOrCreateString(tagValue);
        }

        result[0] = result[0] | 0x08000000;
        result[0] = ByteUtil.byte2int(ByteUtil.reverseBytes(ByteUtil.int2Byte(result[0])));//字节需要翻转一次

        return result;
    }

    protected boolean isStringValue(String value) {
        List<String> FlagString = Arrays.asList("singleTask", "minSdkVersion", "versionCode", "singleTop",
                "standard", "singleInstance", "singleTask");
        if (value.startsWith("@") || value.startsWith("#") || FlagString.contains(value))
            return false;
        return true;
    }

    public int getOrCreateString(String src) {
        return mStringChunk.getOrCreateString(src);
    }

    public AttributeType getType() {
        return mType;
    }

    public String getString(int id) {
        return mStringChunk.getString(id);
    }

    public void setStringById(int id, String value) {
        mStringChunk.setStringById(id, value);
    }

    public int lookingForGoodPosition(List<AttributeData> attributeDataList, String attrName){
        int id = DefaultIdHolder.getInstance().strMap.get(attrName);
        int next = 0;
        for (int i = 0; i < attributeDataList.size(); i++) {
            if(resChunk.isRes(attributeDataList.get(i).name)){
                if(id > resChunk.getResId(attributeDataList.get(i).name)){
                    next = i + 1;
                }else {
                    break;
                }
            }else {
                return i;
            }
        }
        return next;
    }

    public void memAddAttr(String application, String name, String attrValue, int bestPosition) {
        AttributeData data = createAttributeData(name, attrValue);
        data.makeOpt(mStringChunk);
        memAddAttr(application, data, bestPosition);
    }
}

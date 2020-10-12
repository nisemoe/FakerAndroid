package com.facker.toolchain.base.shell.axml;

import com.facker.toolchain.base.shell.axml.struct.AttributeData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GreplayXmlHelper {

    private static Map<String, Object> getLauncherActData(){
        Map<String, Object> mapIntentFilterTag = new HashMap<String, Object>();
        mapIntentFilterTag.put(BinXmlParser.KEY_TAG_NAME, "intent-filter");
        Map<String, Object> mapAction = new HashMap<String, Object>();
        mapAction.put(BinXmlParser.KEY_TAG_NAME, "action");
        mapAction.put("name", "android.intent.action.MAIN");
        mapIntentFilterTag.put(BinXmlParser.KEY_SUB_TAG, mapAction);

        Map<String, Object> mapCategory = new HashMap<String, Object>();
        mapCategory.put(BinXmlParser.KEY_TAG_NAME, "category");
        mapCategory.put("name", "android.intent.category.LAUNCHER");

        mapIntentFilterTag.put(BinXmlParser.KEY_SUB_TAG, Arrays.asList(mapAction,mapCategory));
        return mapIntentFilterTag;
    }


    private static Map<String,Object> getReceverIntent(String action,String data){
    	 Map<String, Object> mapIntentFilterTag = new HashMap<String, Object>();
         mapIntentFilterTag.put(BinXmlParser.KEY_TAG_NAME, "intent-filter");
         Map<String, Object> mapAction = new HashMap<String, Object>();
         mapAction.put(BinXmlParser.KEY_TAG_NAME, "action");
         mapAction.put("name", action);
         mapIntentFilterTag.put(BinXmlParser.KEY_SUB_TAG, mapAction);
         Map<String, Object> mapCategory = new HashMap<String, Object>();
         mapCategory.put(BinXmlParser.KEY_TAG_NAME, "data");
         mapCategory.put("name",data);
         mapIntentFilterTag.put(BinXmlParser.KEY_SUB_TAG, Arrays.asList(mapAction,mapCategory));
         return mapIntentFilterTag;
    }

    /**
     *
     * @param name
     * @param action
     * @param data
     * @return
     */
    private static Map<String, Object> getReceiver(String name,String action,String data){
    	Map<String, Object> mapIntentFilterTag = getReceverIntent(action, data);
    	Map<String, Object> receiver = new HashMap<String, Object>();
    	receiver.put(BinXmlParser.KEY_TAG_NAME, "receiver");
    	receiver.put("name", name);
    	receiver.put(BinXmlParser.KEY_SUB_TAG, Arrays.asList(mapIntentFilterTag));

        return receiver;
    }



    public static void insertReceiver(BinXmlParser parser,String name,String action,String data){
    	parser.insertChunks(parser.createChunks(getReceiver(name, action, data)), true);
    }

    public static void insertMeta(BinXmlParser binXmlParser,String metaKey,String metaValue) {
		Map<String, Object> p1 = new HashMap<String, Object>();
		p1.put(BinXmlParser.KEY_TAG_NAME, "meta-data");
		p1.put("name", metaKey);
		p1.put("value", metaValue);
		if(metaValue!=null&&!"".equals(metaValue)){
			binXmlParser.insertChunks(binXmlParser.createChunks(p1),true);
		}
	}

    public static void insertActivity(BinXmlParser parser, String name){
        insertActivity(parser, name, false, false);
    }

    public static void insertActivity(BinXmlParser parser, String name, boolean isMain){
        insertActivity(parser, name, isMain, false);
    }

    public static void insertActivity(BinXmlParser parser, String name, boolean isMain, boolean isTransparent){
        parser.insertChunks(parser.createChunks(getActivity(name, isMain, isTransparent)), true);
    }

    protected static Map<String, Object> getActivity(String name, boolean isMain, boolean isTransparent){
        Map<String, Object> mapSplashActivity = new HashMap<String, Object>();
        mapSplashActivity.put(BinXmlParser.KEY_TAG_NAME, "activity");
        if(isTransparent){
            mapSplashActivity.put("theme", "@0103000f");
        }
        mapSplashActivity.put("name", name);
        if(isMain){
            mapSplashActivity.put(BinXmlParser.KEY_SUB_TAG, Arrays.asList(getLauncherActData()));
        }
        return mapSplashActivity;
    }


    public static void insertService(BinXmlParser parser, String name, boolean export){
        parser.insertChunks(parser.createChunks(getService(name, export)),true);
    }

    public static void insertProvider(BinXmlParser parser, String name, String authorities){
        parser.insertChunks(parser.createChunks(getProvider(name, authorities)), true);
    }

    public static void insertPermission(BinXmlParser parser, String name){
        parser.insertChunks(parser.createChunks(getPermission(name)), false);
    }

    protected static Map<String, Object> getService(String name,boolean export){
    	LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(BinXmlParser.KEY_TAG_NAME, "service");
        map.put("name", name);
        map.put("exported", export ? "true" : "false");
        return map;
    }

    protected static Map<String, Object> getProvider(String name,String authorities){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(BinXmlParser.KEY_TAG_NAME, "provider");
        map.put("name", name);
        map.put("exported", "false");
        map.put("authorities", authorities);
        map.put("grantUriPermissions", "true");
        return map;
    }

    protected static Map<String, Object> getPermission(String name){
        Map<String, Object> p1 = new HashMap<String, Object>();
        p1.put(BinXmlParser.KEY_TAG_NAME, "uses-permission");
        p1.put("name", name);
        return p1;
    }

    public static void modifyPackageName(BinXmlParser parser, String name){
        AttributeData data = parser.createAttributeData("package", name);
        parser.memModifyAttr("manifest", "", "package", data);
    }

    public static void modifyVersionCode(BinXmlParser parser, Integer versionCode){
        AttributeData data = parser.createAttributeData("versionCode",16,versionCode);
        parser.memModifyAttr("manifest", "", "versionCode", data);
    }

    public static void removeOldLauncherAct(BinXmlParser parser){
        String name = parser.findLauncherActivity();
        parser.memRemoveTag("activity", name,true);
    }

    public static String getPackageName(BinXmlParser parser){
        return parser.memReadAttr("manifest", "", "package");
    }

    public static boolean removeAct(BinXmlParser parser, String name){
        return parser.memRemoveTag("activity", name, false);
    }

    public static String getApplicationName(BinXmlParser parser){
         int id = BinXmlHelper.getApplicationNameId(parser);
         if(id != -1){
             String name = parser.getString(id);
             if(name.startsWith(".")){
                 return getPackageName(parser) + name;
             }else {
                 return name;
             }
         }else {
             return null;
         }
    }

    public static void makeDebuggable(BinXmlParser parser){
        for (AttributeData data : parser.memListAttr("application")) {
            System.out.printf("%d, %d", data.type, data.data);
        }
    }
}

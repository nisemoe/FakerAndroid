package com.facker.toolchain.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AaptApk {

    /**
     *
     * @param rawString
     * @param result = 0 = OK
     */
    public int result;
    public String rawString;

    public String packageName;
    public String versionName;
    public long versionCode;

    public String applicationLabel;
    public String applicationIcon;
    protected String applicationBanner;


    public final List<String> nativeCodes = new ArrayList<>();

    public AaptApk(String rawString, int result){
        this.result = result;
        this.rawString = rawString;
        if ( result == 0 ){
            for (String s: rawString.split("\r\n")){
                if(s.startsWith("package: name=")){
                    Map<String, String> map = new HashMap<>();
                    for (String item : s.split(" ")){
                        if(item.contains("=")){
                            String[] itemArr = item.split("=");
                            if(itemArr.length > 1){
                                map.put(itemArr[0], removeQuote(itemArr[1]));
                            }
                        }
                    }
                    packageName = map.get("name");
                    try {
                        versionCode = Integer.parseInt(map.get("versionCode"));
                    }catch (Exception e){ versionCode = -1; }
                    versionName = map.get("versionName");
                }
                final String application  = "application: ";
                if(s.startsWith(application + "label='")){
                    final Map<String, String> map = readLabel(s);
                    this.applicationLabel = map.get("application: label");
                    this.applicationIcon = map.get("icon");
                    this.applicationBanner = map.get("banner");
                }
                final String nativeStringSignal = "native-code: ";
                if(s.startsWith(nativeStringSignal)){
                    final String nativeCodes = s.substring(nativeStringSignal.length());
                    if(nativeCodes.length() > 3 & nativeCodes.contains(" ")){// "" > "EntityName EntityName"
                        for (String s1 : nativeCodes.split(" ")) {
                            this.nativeCodes.add(removeQuote(s1));
                        }
                    }
                }
            }
        }
    }

    public static Map<String, String> readLabel(String line){
        HashMap<String, String> v = new HashMap<>();
        for (int offset = 0; offset < line.length(); ){
            final int keyPos = line.indexOf("=", offset);
            if(keyPos != -1){
                String key = line.substring(offset, keyPos);
                final int valueEndPos = line.indexOf("'", keyPos + 2);
                if(valueEndPos != -1){
                    final String valueString = line.substring(keyPos + 1, valueEndPos + 1);
                    v.put(key, removeQuote(valueString));
                    offset += ((valueEndPos + 2) - offset);//skip ' and space
                }else {
                    break;
                }
            }else {
                break;
            }
        }
        return v;
    }

//    public final static class KeyValue<K, V>{
//        public final K key;
//        public final V value;
//
//        public KeyValue(K k, V v){
//            this.key = k;
//            this.value = v;
//        }
//    }

    private static String removeQuote(String s){
        if (s.startsWith("'") && s.endsWith("'")){
            int len = s.length() - 2;
            if(len > 0){
                return s.substring(1, s.length() - 1);
            }else{
                return "";
            }
        }
        return s;
    }

}

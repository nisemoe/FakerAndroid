package com.faker.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.unity3d.player.UnityPlayer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Method;

public class JavaPatch {//you can refector this class by you needed
    public static  boolean onKeyDown(Activity activity ,int keyCode, KeyEvent event)   {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            if(activity.getPackageName().endsWith(".nearme.gamecenter")){
                try {
                    Log.i("UnityPlayerActivity","调用返回键");
                    Class<?> threadClazz = Class.forName("com.nearme.offlinesdk.bridge.LocalBridge");
                    Method method = threadClazz.getMethod("backDown", Activity.class);
                    method.invoke(null,activity);
                } catch (Exception e) {
                    Log.i("UnityPlayerActivity","返回键反射调用失败");
                    e.printStackTrace();
                }
                return true;
            }

            if(activity.getPackageName().endsWith(".vivo")){
                try {
                    Log.i("UnityPlayerActivity","调用返回键");
                    Class<?> threadClazz = Class.forName("com.vivo.offline.bridge.LocalBridge");
                    Method method = threadClazz.getMethod("backDown", Activity.class);
                    method.invoke(null,activity);
                } catch (Exception e) {
                    Log.i("UnityPlayerActivity","返回键反射调用失败");
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }
    public static void callDispacher(String triggerTag, Activity activity, Object triggerCallBack){
        try {
            Class coreADManagerClass = Class.forName("com.play.sdkcore.CoreADManager");
            Object coreADManager = coreADManagerClass.newInstance();
            Method method = coreADManagerClass.getDeclaredMethod("onTrigger",String.class,Activity.class,Object.class);
            method.invoke(coreADManager,triggerTag,activity,triggerCallBack);
        } catch (Exception e) {
            if(triggerTag!=null){
               if(triggerTag.startsWith(Constant.MSG_TRIGGER_TAG_REWARD_PREFIX)){
                   Toast.makeText(activity,"暂无视频内容",Toast.LENGTH_SHORT).show();
               }
            }
            e.printStackTrace();
        }
    }
    public static void addCoin() {// you should offer a  fake method to called by this method
        UnityPlayer.UnitySendMessage("GAME CONTROLLER","ResetGame","");
    }
    public static void goToPrivacyPage(Context context) {
//        Intent intent = new Intent(context,WebViewActivity.class);
//        intent.putExtra("url","http://ianpei.com/policy/privacy");
//        intent.putExtra("title","隐私政策");
//        context.startActivity(intent);
    }
    public static void goToTermsPage(Context context) {
//        Intent intent = new Intent(context,WebViewActivity.class);
//        intent.putExtra("url","https://ianpei.com/policy/agreement");
//        intent.putExtra("title","服务协议");
//        context.startActivity(intent);
    }

    public static void goToMoreGame(Context context) {
        if(context.getPackageName().endsWith(".nearme.gamecenter")){
            try {
                Log.i("UnityPlayerActivity","moreGame");
                Class<?> threadClazz = Class.forName("com.nearme.offlinesdk.bridge.LocalBridge");
                Method method = threadClazz.getMethod("moreGame");
                method.invoke(null);
            } catch (Exception e) {
                Log.i("UnityPlayerActivity","moregame 反射调用失败");
                e.printStackTrace();
            }
        }
    }
    public static void rateUs(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void coverPlayerprefs(Context context,String fileName) {
        try {

            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName()+".v2.playerprefs", Context.MODE_PRIVATE); //私有数据
            if(!sharedPreferences.getBoolean("first",true)){
                return;
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
            editor.putBoolean("first",false);


            String xml = getFromAssets(context,fileName);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG://开始解析
                        if ("int".equals(nodeName)) {
                            String name = parser.getAttributeValue(null, "name");
                            int value = Integer.valueOf(parser.getAttributeValue(null, "value"));
                            editor.putInt(name, value);
                        }
                        if("float".equals(nodeName)){
                            String name = parser.getAttributeValue(null, "name");
                            float value = Float.valueOf(parser.getAttributeValue(null, "value"));
                            editor.putFloat(name, value);
                        }
                        if("string".equals(nodeName)){
                            String name = parser.getAttributeValue(null, "name");
                            String value = parser.nextText();
                            editor.putString(name, value);
                        }
                        break;
                    case XmlPullParser.END_TAG://完成解析
                        break;
                    default:
                        break;
                }
                eventType=parser.next();
            }
            editor.commit();//提交修改
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFromAssets(Context context,String fileName){
        try {
            InputStreamReader inputReader = new InputStreamReader( context.getResources().getAssets().open(fileName) );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static class Constant {
        public static String MSG_TRIGGER_TAG_COMMON_PREFIX = "msg_trigger_tag_common_";
        public static String MSG_TRIGGER_TAG_REWARD_PREFIX ="msg_trigger_tag_reward_";
        public static String MSG_TRIGGER_LOCAL_PRIVACY ="msg_trigger_local_privacy";
        public static String MSG_TRIGGER_LOCAL_TERMS ="msg_trigger_local_terms";
        public static String MSG_TRIGGER_LOCAL_TATE ="msg_trigger_local_rate";
        public static String MSG_TRIGGER_LOCAL_MORE_GAME ="msg_trigger_tag_more_game";
    }
}

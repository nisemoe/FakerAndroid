package com.faker.android;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;
import {R};
import org.json.JSONObject;
import java.util.Map;

public class FakerUnityActivity extends com.unity3d.player.UnityPlayerActivity {
    public native String init();
    static final int HANDLER_MSG_CALLJAVA = 1000;
    final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_MSG_CALLJAVA:
                    String cmsg = (String) msg.obj;
                    callJava(cmsg);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //FakeInt
        init();
        //Init playerprefs if you need
        JavaPatch.coverPlayerprefs(this,"init.xml");


        imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.splash);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mUnityPlayer.addViewToPlayer(imageView,false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mUnityPlayer.removeViewFromPlayer(imageView);
            }
        },3000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)   {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            return JavaPatch.onKeyDown(this,keyCode,event);
        }
        return mUnityPlayer.injectEvent(event);
    }
    //Called By Faker
    public void onCall(String msg) {// unity player isnot main thread transfer method to main thread
        Message message = new Message();
        message.what =HANDLER_MSG_CALLJAVA;
        message.obj = msg;
        handler.sendMessage(message);
    }

    private void callJava(String msg){
        Logger.log(msg);
        if(JavaPatch.Constant.MSG_TRIGGER_LOCAL_PRIVACY.equals(msg)){
            JavaPatch.goToPrivacyPage(this);
        }
        if(JavaPatch.Constant.MSG_TRIGGER_LOCAL_TERMS.equals(msg)){
            JavaPatch.goToTermsPage(this);
        }
        if(JavaPatch.Constant.MSG_TRIGGER_LOCAL_MORE_GAME.equals(msg)){
            JavaPatch.goToMoreGame(this);
        }
        if(JavaPatch.Constant.MSG_TRIGGER_LOCAL_TATE.equals(msg)){
            JavaPatch.rateUs(this);
        }
        if(msg.startsWith(JavaPatch.Constant.MSG_TRIGGER_TAG_COMMON_PREFIX)){
            //TODO
        }
        if(msg.startsWith(JavaPatch.Constant.MSG_TRIGGER_TAG_REWARD_PREFIX)){
            //TODO
            JavaPatch.callDispacher(msg,this,this);
        }
    }
}

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
import java.util.Map;
public class FakerActivity extends Activity {
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
        init();
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
    }
}

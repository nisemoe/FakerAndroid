package com.faker.android;
import android.app.Application;
import android.content.Context;

public class FakerApp extends Application {
    static {
        System.loadLibrary("native-lib");
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        fakeDex(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Application app = this.fakeApp(this);
        if(app!=null){
            app.onCreate();
        }
    }

    private native Application fakeApp(Application application);

    private native void fakeDex(Context base);
}

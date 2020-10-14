package com.faker.android;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
        Application app = this.injectTargetApplication(this,"{APPLICATION_NAME}");
        if(app!=null){
            fakeApp(app,true);
        }else {
            fakeApp(this,false);
        }
    }

    private native void fakeApp(Application application,Boolean b);

    private native void fakeDex(Context base);

    private Application injectTargetApplication(Context context,String appClassName){

        if(TextUtils.isEmpty(appClassName)){
            return null;
        }
        if(appClassName.equals(this.getClass().getName())){
            return null;
        }

        Object currentActivityThread = RefInvoke.invokeStaticMethod("android.app.ActivityThread", "currentActivityThread",new Class[] {}, new Object[] {});
        Object mBoundApplication = RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread,"mBoundApplication");
        Object loadedApkInfo = RefInvoke.getFieldOjbect("android.app.ActivityThread$AppBindData",mBoundApplication, "info");

        RefInvoke.setFieldOjbect("android.app.LoadedApk", "mApplication",loadedApkInfo, null);
        Object oldApplication = RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread,"mInitialApplication");

        @SuppressWarnings("unchecked")
        ArrayList<Application> mAllApplications = (ArrayList<Application>) RefInvoke.getFieldOjbect("android.app.ActivityThread",currentActivityThread, "mAllApplications");
        mAllApplications.remove(oldApplication);
        ApplicationInfo appinfo_In_LoadedApk = (ApplicationInfo) RefInvoke.getFieldOjbect("android.app.LoadedApk", loadedApkInfo,"mApplicationInfo");
        ApplicationInfo appinfo_In_AppBindData = (ApplicationInfo) RefInvoke.getFieldOjbect("android.app.ActivityThread$AppBindData",mBoundApplication, "appInfo");
        appinfo_In_LoadedApk.className = appClassName;
        appinfo_In_AppBindData.className = appClassName;

        Application app = (Application) RefInvoke.invokeMethod("android.app.LoadedApk", "makeApplication", loadedApkInfo,new Class[] { boolean.class, Instrumentation.class },
                new Object[] { false, null });
        RefInvoke.setFieldOjbect("android.app.ActivityThread","mInitialApplication", currentActivityThread, app);

        Iterator<?> it;
        if(Build.VERSION.SDK_INT < 19){
            HashMap<?, ?> mProviderMap = (HashMap<?, ?>) RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread,"mProviderMap");
            it = mProviderMap.values().iterator();
        }else{
            ArrayMap<?, ?> mProviderMap = (ArrayMap<?, ?>) RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread,"mProviderMap");
            it = mProviderMap.values().iterator();
        }
        while (it.hasNext()) {
            Object providerClientRecord = it.next();
            Object localProvider = RefInvoke.getFieldOjbect("android.app.ActivityThread$ProviderClientRecord",providerClientRecord, "mLocalProvider");
            RefInvoke.setFieldOjbect("android.content.ContentProvider","mContext", localProvider, app);
        }
        return app;
    }
}

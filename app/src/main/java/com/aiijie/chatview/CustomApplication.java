package com.aiijie.chatview;

import android.app.Application;

/**
 * Created by klx on 2017/6/28.
 */

public class CustomApplication extends Application{
    private static CustomApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static Application getApplication(){
        return application;
    }
}

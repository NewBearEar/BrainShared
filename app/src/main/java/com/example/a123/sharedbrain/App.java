package com.example.a123.sharedbrain;

import android.app.Application;

import android.content.Context;
/**
 * 启动的Application
 */
public class App extends Application {

    private static App app;

    /**
     * 获取Application Context
     */
    public static Context getAppContext() {
        return app != null ? app.getApplicationContext() : null;
    }
}

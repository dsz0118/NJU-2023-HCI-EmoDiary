package com.example.calendar.appContext;

import android.app.Application;
import android.content.Context;

public class MyContext extends Application {
    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = getApplicationContext();
    }

    public static Context getContext()
    {
        return instance;
    }
}

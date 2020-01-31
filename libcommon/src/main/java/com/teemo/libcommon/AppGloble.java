package com.teemo.libcommon;

import android.app.Application;

import java.lang.reflect.Method;

public class AppGloble {
    private static Application sApplication;
    public static Application getApplication(){
        if(sApplication==null){
            try {
                Method method = Class.forName("android.app.ActivityThread").getDeclaredMethod("currentApplication");
                sApplication = (Application) method.invoke(null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sApplication;
    }
}

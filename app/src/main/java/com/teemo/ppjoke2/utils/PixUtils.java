package com.teemo.ppjoke2.utils;

import android.util.DisplayMetrics;

import com.teemo.libcommon.AppGloble;

public class PixUtils {
    public static int dp2px(int dpValue) {
        DisplayMetrics displayMetrics = AppGloble.getApplication().getResources().getDisplayMetrics();
        return (int) (displayMetrics.density * dpValue + 0.5f);
    }


    public static int getScreenWidth() {
        DisplayMetrics displayMetrics = AppGloble.getApplication().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getScreeHeight() {
        DisplayMetrics displayMetrics = AppGloble.getApplication().getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

}

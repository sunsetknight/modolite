package com.mingko.modolite.util;

import android.content.Context;

/**
 * Toast工具类
 *
 * Created by SunsetKnight on 2016/9/12.
 */
public class Toast {

    private static Context context;

    public static void init(Context context) {
        Toast.context = context;
    }

    public static void show(String msg) {
        android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_SHORT).show();
    }

}
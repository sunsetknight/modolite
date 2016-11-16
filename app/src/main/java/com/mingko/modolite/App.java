package com.mingko.modolite;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.mingko.modolite.util.Toast;

import timber.log.Timber;

/**
 * 启动Application
 *
 * Created by SunsetKnight on 2016/7/25.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //讯飞语音 APPID = 570e0846 (old version: 56a6efef)
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=570e0846");

        //初始化 Log 日志工具 Timber
        Timber.plant(new Timber.DebugTree());

        //activeAndroid数据库
        ActiveAndroid.initialize(this);

        //初始化Toast
        Toast.init(this);

    }
}
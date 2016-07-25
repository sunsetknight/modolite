package com.mingko.moduolite;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

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

        //讯飞语音
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=56a6efef");

        //初始化 log
        Timber.plant(new Timber.DebugTree());

        //activeAndroid数据库
        ActiveAndroid.initialize(this);

    }
}

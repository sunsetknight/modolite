package com.mingko.modolite.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

import com.mingko.modolite.util.Toast;

/**
 * 拦截WebView 的onTouchEvent事件
 *
 * Created by SunsetKnight on 2016/11/7.
 */

public class InterceptTouchWebView extends WebView{

    public InterceptTouchWebView(Context context) {
        super(context);
    }

    public InterceptTouchWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptTouchWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 将WebView 的点击事件进行拦截
     *
     * @return false 表示拦截
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Toast.show("点击了HTML页面");
        return false;
    }

}
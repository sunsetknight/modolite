package com.mingko.modolite.control.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mingko.modolite.model.event.NetworkStateChangeEvent;
import com.mingko.modolite.util.NetUtil;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * 网络状态的监听:
 * 抛出事件:    NetworkStateChangeEvent
 * Created by ssthouse on 2015/12/23.
 */
public class NetStateReceiver extends BroadcastReceiver {

    /**
     * 监听的action
     */
    private static String NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    @Override
    public void onReceive(Context context, Intent intent) {
        int networkState = NetUtil.NETWORK_NONE;
        if (intent.getAction().equals(NET_CHANGE_ACTION)) {
            networkState = NetUtil.getNetworkState(context);
        }
        switch (networkState) {
            case NetUtil.NETWORK_NONE:
                EventBus.getDefault().post(new NetworkStateChangeEvent(NetworkStateChangeEvent.NetworkState.NONE));
                Timber.e("当前无网络连接");
                break;
            case NetUtil.NETWORK_MOBILE:
                EventBus.getDefault().post(new NetworkStateChangeEvent(NetworkStateChangeEvent.NetworkState.MOBILE));
                Timber.e("当前使用 手机流量");
                break;
            case NetUtil.NETWORK_WIFI:
                EventBus.getDefault().post(new NetworkStateChangeEvent(NetworkStateChangeEvent.NetworkState.WIFI));
                Timber.e("当前使用 WIFI 网络");
                break;
        }
    }
}
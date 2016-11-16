package com.mingko.modolite.fragment;

import android.content.Context;
import android.os.Message;

import com.mingko.modolite.control.tcp.TCPClient;
import com.mingko.modolite.model.DbHelper;
import com.mingko.modolite.model.ModoModel;
import com.mingko.modolite.model.bean.MsgBean;
import com.mingko.modolite.model.event.SpeechUnderstandEvent;

import java.util.List;

import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * presenter
 * Created by ssthouse on 2016/2/14.
 */
public class ModoPresenter {

    //View Model
    private ModoFragment mModoFragmentView;
    private ModoModel mModoModel;

    private Context mContext;

    //构造方法
    public ModoPresenter(Context context, ModoFragment modoFragment) {
        this.mContext = context;
        this.mModoFragmentView = modoFragment;
        mModoModel = new ModoModel();
        EventBus.getDefault().register(this);
    }

    //加载更多msg
    public void loadMoreMsg() {
        mModoModel.loadMoreMsg(mModoFragmentView.getTopMsgBean())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<MsgBean>>() {
                    @Override
                    public void call(List<MsgBean> msgBeans) {
                        //刷新UI
                        mModoFragmentView.loadMoreMsg(msgBeans);
                    }
                });
    }

    //添加msgBen回调
    public void onEventMainThread(MsgBean msgBean) {
        Timber.e(msgBean.toString());
        if (msgBean == null) {
            return;
        }
        //添加数据到对话框
        mModoFragmentView.addMsgBean(msgBean);
        //保存到数据库
        DbHelper.saveMsgBean(msgBean);
    }

    //语义理解回调
    public void onEventMainThread(SpeechUnderstandEvent event) {
        Message msg = new Message();
        msg.what = TCPClient.MESSAGE_SEND;
        msg.obj = event.getJsonResult();
        mModoFragmentView.getClientThread().revHandler.sendMessage(msg);
    }

    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

}
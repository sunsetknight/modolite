package com.mingko.moduolite.fragment.audiorecord.presenter;

import android.content.Context;
import android.os.Message;

import com.mingko.moduolite.control.TCPClient;
import com.mingko.moduolite.fragment.ModuoFragment;
import com.mingko.moduolite.fragment.audiorecord.model.ModoModel;
import com.mingko.moduolite.fragment.audiorecord.model.event.SpeechUnderstandEvent;
import com.mingko.moduolite.fragment.audiorecord.presenter.util.DbHelper;
import com.mingko.moduolite.fragment.audiorecord.view.adapter.MsgBean;

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
public class ModuoPresenter {

    //View Model
    private ModuoFragment mModuoFragmentView;
    private ModoModel mModuoModel;

    private Context mContext;

    //构造方法
    public ModuoPresenter(Context context, ModuoFragment moduoFragment) {
        this.mContext = context;
        this.mModuoFragmentView = moduoFragment;
        mModuoModel = new ModoModel();
        EventBus.getDefault().register(this);
    }

    //加载更多msg
    public void loadMoreMsg() {
        mModuoModel.loadMoreMsg(mModuoFragmentView.getTopMsgBean())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<MsgBean>>() {
                    @Override
                    public void call(List<MsgBean> msgBeans) {
                        //刷新UI
                        mModuoFragmentView.loadMoreMsg(msgBeans);
                    }
                });
    }

    //添加msgBen回调
    public void onEventMainThread(MsgBean msgBean) {
        Timber.e(msgBean.toString());
        if (msgBean == null || msgBean.getContent().length() <= 0) {
            return;
        }
        //添加数据到对话框
        mModuoFragmentView.addMsgBean(msgBean);
        //保存到数据库
        DbHelper.saveMsgBean(msgBean);
    }

    //语义理解回调
    public void onEventMainThread(SpeechUnderstandEvent event) {
        Message msg = new Message();
        msg.what = TCPClient.MESSAGE_SEND;
        msg.obj = event.getJsonResult();
        mModuoFragmentView.getClientThread().revHandler.sendMessage(msg);
    }

    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

}

package com.mingko.moduolite.fragment.widget.record.presenter;

import android.content.Context;
import android.widget.Toast;

import com.mingko.moduolite.fragment.ModuoFragment;
import com.mingko.moduolite.fragment.widget.record.model.ModuoModel;
import com.mingko.moduolite.fragment.widget.record.model.event.SpeechUnderstandEvent;
import com.mingko.moduolite.fragment.widget.record.presenter.util.DbHelper;
import com.mingko.moduolite.fragment.widget.record.view.adapter.MsgBean;

import java.util.List;

import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * presenter
 * Created by ssthouse on 2016/2/14.
 */
public class ModuoPresenter {

    //View Model
    private ModuoFragment mModuoFragmentView;
    private ModuoModel mModuoModel;

    private Context mContext;

    //构造方法
    public ModuoPresenter(Context context, ModuoFragment moduoFragment) {
        this.mContext = context;
        this.mModuoFragmentView = moduoFragment;
        mModuoModel = new ModuoModel();
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
        if (msgBean == null) {
            return;
        }
        //添加数据到对话框
        mModuoFragmentView.addMsgBean(msgBean);
        //保存到数据库
        DbHelper.saveMsgBean(msgBean);
    }

    //语义理解回调
    public void onEventMainThread(SpeechUnderstandEvent event) {
        Toast.makeText(mContext, event.getJsonResult(), Toast.LENGTH_SHORT).show();
    }

    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

}

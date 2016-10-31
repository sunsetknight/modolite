package com.mingko.moduolite.fragment.audiorecord.model;


import com.mingko.moduolite.fragment.audiorecord.presenter.util.DbHelper;
import com.mingko.moduolite.fragment.audiorecord.view.adapter.MsgBean;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 数据源
 * Created by ssthouse on 2016/2/14.
 */
public class ModoModel {

    //获取最近10条msg
    public Observable<List<MsgBean>> loadMoreMsg(MsgBean latestMsgBean){
        return Observable.just(latestMsgBean)
                .map(new Func1<MsgBean, List<MsgBean>>() {
                    @Override
                    public List<MsgBean> call(MsgBean msgBean) {
                        return DbHelper.getLastTenMsgBean(msgBean);
                    }
                });

    }
}

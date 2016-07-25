package com.mingko.moduolite.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.mingko.moduolite.R;
import com.mingko.moduolite.fragment.widget.record.presenter.ModuoPresenter;
import com.mingko.moduolite.fragment.widget.record.view.adapter.ChatLvAdapter;
import com.mingko.moduolite.fragment.widget.record.view.adapter.MsgBean;
import com.mingko.moduolite.fragment.widget.record.view.widget.record.RecordButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

/**
 * 魔哆主界面
 * Created by ssthouse on 2016/1/24.
 */
public class ModuoFragment extends Fragment {

    //Presenter
    private ModuoPresenter mModuoPresenter;

    //录音按钮
    @BindView(R.id.id_btn_record)
    RecordButton btnRecord;

    //聊天列表
    @BindView(R.id.id_swipe_layout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.id_recycle_chat)
    RecyclerView recycleChat;
    ChatLvAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_moduo, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        //presenter
        mModuoPresenter = new ModuoPresenter(getContext(), this);
        return rootView;
    }

    private void initView() {
        //聊天列表
        Context context = getContext();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recycleChat.setLayoutManager(linearLayoutManager);
        mAdapter = new ChatLvAdapter(getContext(), recycleChat);
        recycleChat.setAdapter(mAdapter);
        recycleChat.setItemAnimator(new LandingAnimator(new AccelerateDecelerateInterpolator()));

        //刷新响应
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mModuoPresenter.loadMoreMsg();
            }
        });

    }

    public void loadMoreMsg(List<MsgBean> msgList) {
        if (msgList == null || msgList.size() == 0) {
            Toast.makeText(getContext(), "没有更多记录了", Toast.LENGTH_SHORT).show();
        } else {
            mAdapter.addOldMsg(msgList);
        }
        //清除刷新状态
        swipeLayout.setRefreshing(false);
    }

    public void addMsgBean(MsgBean msgBean) {
        mAdapter.addNewMsg(msgBean);
    }

    public MsgBean getTopMsgBean() {
        return mAdapter.getMsgList().get(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mModuoPresenter.destroy();
    }
}

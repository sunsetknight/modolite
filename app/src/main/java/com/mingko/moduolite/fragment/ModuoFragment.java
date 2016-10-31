package com.mingko.moduolite.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.mingko.moduolite.activity.MainActivity;
import com.mingko.moduolite.control.TCPClient;
import com.mingko.moduolite.fragment.audiorecord.presenter.ModuoPresenter;
import com.mingko.moduolite.fragment.audiorecord.view.adapter.ChatLvAdapter;
import com.mingko.moduolite.fragment.audiorecord.view.adapter.MsgBean;
import com.mingko.moduolite.fragment.audiorecord.view.widget.record.RecordButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import timber.log.Timber;

/**
 * 魔哆主界面
 * Created by ssthouse on 2016/1/24.
 */
public class ModuoFragment extends Fragment {

    //Presenter
    private ModuoPresenter mModuoPresenter;
    private static Handler handler;
    // 定义与服务器通信的子线程
    private static TCPClient clientThread;

    //录音按钮
    @BindView(R.id.id_btn_record)
    RecordButton btnRecord;

    //聊天列表
    @BindView(R.id.id_swipe_layout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.id_recycle_chat)
    RecyclerView recycleChat;
    ChatLvAdapter mAdapter;

    /**
     * 初始化客户端连接线程
     * 收到来自服务端云魔哆的数据后原文显示
     */
    public void initClientThread() {
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == TCPClient.MESSAGE_RECEIVED){
                    Timber.e(msg.obj.toString());
                    EventBus.getDefault().post(MsgBean.createModoTextAnswer(msg.obj.toString()));
                }
            }
        };
        clientThread = new TCPClient(MainActivity.IP, MainActivity.PORT, handler);
        new Thread(clientThread).start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_moduo, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        initClientThread();
        mModuoPresenter = new ModuoPresenter(getContext(), this);
        return rootView;
    }

    private void initView() {
        //聊天列表
        final Context context = getContext();
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

    public TCPClient getClientThread() {
        return clientThread;
    }

    public static void resetClientThread(String ip, int port) {
        endClientThread();
        clientThread = new TCPClient(ip, port, handler);
        new Thread(clientThread).start();
    }

    public static void endClientThread() {
        clientThread.endLife();
    }
}

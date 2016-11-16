package com.mingko.modolite.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import com.mingko.modolite.R;
import com.mingko.modolite.model.event.AudioPreparedEvent;
import com.mingko.modolite.model.event.VolumeChangEvent;
import com.mingko.modolite.view.adapter.SpeechManagerImpl;
import com.mingko.modolite.model.bean.MsgBean;
import com.mingko.modolite.view.adapter.AudioDialogManager;

import de.greenrobot.event.EventBus;

/**
 * 录音按钮
 * Created by ssthouse on 2016/1/23.
 */
public class RecordButton extends Button {

    //dialog管理器
    private AudioDialogManager mAudioDialogManager;
    //语音管理器
    private SpeechManagerImpl mSpeechManagerImpl;

    //手指按下的时间
    private long touchDownTime;

    //目前按钮状态
    private State currentState = State.STATE_NORMAL;

    public enum State {
        //正常    正在录音    想取消
        STATE_NORMAL, STATE_RECORDING, STATE_WANT_CANCEL
    }

    /**
     * 只能在xml用的构造方法
     */
    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        mAudioDialogManager = new AudioDialogManager(getContext());
        mSpeechManagerImpl = SpeechManagerImpl.getInstance(getContext());
    }

    /**
     * 录音准备完毕事件
     *
     * @param event
     */
    public void onEventMainThread(AudioPreparedEvent event) {
        //显示正在录音状态
        changeState(State.STATE_RECORDING);
    }

    //音量变化事件
    public void onEventMainThread(VolumeChangEvent event) {
        if (currentState == State.STATE_RECORDING) {
            mAudioDialogManager.updateVolumeLevel(mSpeechManagerImpl.getVolumeLevel(7));
        }
    }

    //改变按钮状态
    public void changeState(State newState) {
        if (currentState == newState) {
            return;
        }
        //更新状态
        currentState = newState;
        switch (newState) {
            case STATE_NORMAL:
                setText(R.string.str_record_normal);
                setBackgroundResource(R.drawable.btn_record_normal);
                mAudioDialogManager.dismiss();
                break;
            case STATE_RECORDING:
                setText(R.string.str_record_recording);
                mAudioDialogManager.showRecordingDialog();
                setBackgroundResource(R.drawable.btn_record_recording);
                break;
            case STATE_WANT_CANCEL:
                setText(R.string.str_record_want_cancel);
                setBackgroundResource(R.drawable.btn_record_recording);
                mAudioDialogManager.showWantCancelDialog();
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //开始录音
                mSpeechManagerImpl.startSpeech();
                //记下时间戳
                touchDownTime = System.currentTimeMillis();
                //转为录音状态
                changeState(State.STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:
                if (isWantCancel(x, y)) {
                    changeState(State.STATE_WANT_CANCEL);
                } else {
                    changeState(State.STATE_RECORDING);
                }
                break;
            case MotionEvent.ACTION_UP:
                changeState(State.STATE_NORMAL);
                if (isWantCancel(x, y)) {
                    mSpeechManagerImpl.cancelSpeech();
                } else {
                    if ((System.currentTimeMillis() - touchDownTime) < 500) {
                        mSpeechManagerImpl.cancelSpeech();
                        Toast.makeText(getContext(), "多说几句吧", Toast.LENGTH_SHORT).show();
                    } else {
                        mSpeechManagerImpl.stopSpeech();
                        //添加一条消息
                        MsgBean msgBean = new MsgBean(MsgBean.FROM_USER, MsgBean.TYPE_AUDIO,
                                mSpeechManagerImpl.getCurrentFilePath(),
                                (int)(System.currentTimeMillis()-touchDownTime)/1000,
                                MsgBean.STATE_SENDING);
                        //audio消息时长
                        EventBus.getDefault().post(msgBean);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    //判断是否是要取消
    private boolean isWantCancel(int x, int y) {
        return x < 0 || x > getWidth() || y < -50 || y > getHeight() + 50;
    }

}
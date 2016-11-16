package com.mingko.modolite.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mingko.modolite.R;
import com.mingko.modolite.view.adapter.AudioPlayer;
import com.mingko.modolite.model.bean.MsgBean;
import com.mingko.modolite.util.DimenUtil;

/**
 * 用在xml里面
 * Created by ssthouse on 2016/1/25.
 */
public class AudioPlayButton extends TextView {

    //最长20秒对应的margin
    private int MAX_MARGIN_LEFT;
    private static final int MAX_AUDIO_LENGTH = 20;

    private MsgBean audioMsg;
    private State currentState;

    enum State {
        STATE_NORMAL, STATE_PLAYING
    }

    public AudioPlayButton(Context context) {
        this(context, null);
    }

    public AudioPlayButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //最大距离宽度
        MAX_MARGIN_LEFT = DimenUtil.dp2px(getContext(), 100);
        //初始状态
        currentState = State.STATE_NORMAL;
    }

    //更新控件宽度
    public void updateView() {
        if (audioMsg == null) {
            return;
        }
        //画出默认背景
        stopAnim();
        //改变长度
        int currentMargin;
        if (audioMsg.getAudioDuration() > MAX_AUDIO_LENGTH) {
            currentMargin = MAX_MARGIN_LEFT;
        } else {
            currentMargin = (int) (audioMsg.getAudioDuration() / 20.0f * MAX_MARGIN_LEFT);
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
        layoutParams.setMargins(currentMargin, 0, 0, 0);
        setLayoutParams(layoutParams);
    }

    public void onClick() {
        if (audioMsg == null) {
            return;
        }
        switch (currentState){
            case STATE_NORMAL:
                currentState = State.STATE_PLAYING;
                AudioPlayer.getInstance(getContext()).playAudio(audioMsg.getContent(), this);
                playAnim();
                break;
            case STATE_PLAYING:
                currentState = State.STATE_NORMAL;
                AudioPlayer.getInstance(getContext()).pausePlayer();
                stopAnim();
                break;
        }
    }

    public void playAnim() {
        currentState = State.STATE_PLAYING;
        if (audioMsg.isFromModo()) {
            setBackgroundResource(R.drawable.anim_audio_play_btn_left);
        } else {
            setBackgroundResource(R.drawable.anim_audio_play_btn_right);
        }
        AnimationDrawable animationDrawable = (AnimationDrawable) getBackground();
        animationDrawable.start();
    }

    public void stopAnim() {
        currentState = State.STATE_NORMAL;
        if (audioMsg.isFromModo()) {
            setBackgroundResource(R.drawable.ic_chat_voice_left);
        } else {
            setBackgroundResource(R.drawable.ic_chat_voice_right);
        }
    }

    //getter---------------setter-----------------------------------------------

    public MsgBean getAudioMsg() {
        return audioMsg;
    }

    public void setAudioMsg(MsgBean audioMssg) {
        this.audioMsg = audioMssg;
    }
}
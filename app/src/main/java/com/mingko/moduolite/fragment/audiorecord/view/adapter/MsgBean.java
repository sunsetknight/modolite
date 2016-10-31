package com.mingko.moduolite.fragment.audiorecord.view.adapter;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 消息数据bean
 *
 * 数据库表：Message
 * 列：
 *     msg来源
 *     msg类型
 *     msg内容
 *     msg时长（仅限于录音文件）
 *     msg时间戳
 *     msg发送状态
 *
 * Created by ssthouse on 2016/1/25.
 */
@Table(name = MsgBean.TABLE_MSG)
public class MsgBean extends Model{

    //数据表
    public static final String TABLE_MSG = "Message";
    public static final String COLUMN_IS_FROM_MODO = "IsFromModo";
    public static final String COLUMN_MSG_TYPE = "MsgType";
    public static final String COLUMN_CONTENT = "Content";
    public static final String COLUMN_AUDIO_DURATION = "AudioDuration";
    public static final String COLUMN_TIME_STAMP = "TimeStamp";
    public static final String COLUMN_MSG_STATE = "MsgState";

    //消息来源
    public static final boolean FROM_MODO = true;
    public static final boolean FROM_USER = false;

    //消息类型
    public static final int TYPE_TEXT = 1000;
    public static final int TYPE_HTML = 1001;
    public static final int TYPE_AUDIO = 1002;
    public static final int TYPE_IMAGE = 1003;

    //消息状态
    public static final int STATE_SENT = 1100;
    public static final int STATE_SENDING = 1101;
    public static final int STATE_FILED = 1102;
    public static final int STATE_RECEIPT = 1103;

    //根据数据库建表
    @Column(name = COLUMN_IS_FROM_MODO)
    private boolean isFromModo;
    @Column(name = COLUMN_MSG_TYPE)
    private int msgType;
    @Column(name = COLUMN_CONTENT)
    private String content;
    @Column(name = COLUMN_AUDIO_DURATION)
    private int audioDuration;
    @Column(name = COLUMN_TIME_STAMP)
    private long timeStamp;
    @Column(name = COLUMN_MSG_STATE)
    private int msgState;

    //无参构造方法
    public MsgBean() {
        super();
    }

    //构造方法
    public MsgBean(boolean isFromModo, int msgType, String content, int msgState) {
        this(isFromModo, msgType, content, 0, msgState);
    }

    //主要用于语音消息的构造
    public MsgBean(boolean isFromModo, int msgType, String content, int audioDuration, int msgState) {
        this.isFromModo = isFromModo;
        this.msgType = msgType;
        this.content = content;
        this.audioDuration = audioDuration;
        this.timeStamp = System.currentTimeMillis();
        this.msgState = msgState;
    }

    public static MsgBean createModoTextAnswer(String content){
        return new MsgBean(MsgBean.FROM_MODO, TYPE_TEXT, content, 0, MsgBean.STATE_SENT);
    }

    public boolean isFromModo() {
        return isFromModo;
    }

    public int getMsgType() {
        return msgType;
    }

    public String getContent() {
        return content;
    }

    public int getAudioDuration() {
        return audioDuration;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getMsgState() {
        return msgState;
    }

}

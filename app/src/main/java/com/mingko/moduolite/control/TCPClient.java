package com.mingko.moduolite.control;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.mingko.moduolite.fragment.widget.record.view.adapter.MsgBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * TCP 传输协议，与云魔哆连接
 *
 * Created by SunsetKnight on 2016/7/29.
 */
public class TCPClient implements Runnable {

    public static final int MESSAGE_SEND = 1;
    public static final int MESSAGE_RECEIVED = 2;

    /** 首次连接的确认文字 */
    private static final String CONNECT_VERIFY = "服务器连接成功";
    /** 监听服务器端的数据时最长等待时间 */
    private static final int SOCKET_TIMEOUT = 1 * 60 * 1000;

    public String ip;
    public int port;
    private boolean life = true;

    private Socket socket;
    private BufferedReader br = null;
    private OutputStream os = null;
    private Handler handler;
    public Handler revHandler;

    public TCPClient(String ip, int port,Handler handler ){
        this.ip = ip;
        this.port = port;
        this.handler = handler;
        Timber.e("启动 Tcp Client");
    }

    @Override
    public void run() {
        try {
            socket = new Socket(ip, port);
            socket.setSoTimeout(SOCKET_TIMEOUT);//每5分钟检查一次连接
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os = socket.getOutputStream();
        } catch ( UnknownHostException e) {
            Timber.e("未能识别的服务器地址");
            EventBus.getDefault().post(MsgBean.getInstance(MsgBean.TYPE_MODUO_TEXT, MsgBean.STATE_SENT, "未能识别的服务器地址，请重新设置IP"));
            endLife();
            return;
        } catch ( ConnectException e){
            Timber.e("未能连接到指定服务器");
            EventBus.getDefault().post(MsgBean.getInstance(MsgBean.TYPE_MODUO_TEXT, MsgBean.STATE_SENT, "未能连接到指定服务器，请重新设置连接"));
            endLife();
            return;
        } catch ( IOException e) {
            Timber.e("服务器连接失败");
            EventBus.getDefault().post(MsgBean.getInstance(MsgBean.TYPE_MODUO_TEXT, MsgBean.STATE_SENT, "服务器连接失败，请确认服务器是否开启。"));
            endLife();
            return;
        }

        // 启动一条子线程来读取服务器响应的数据
        new Thread() {
            @Override
            public void run() {
                Timber.e("开启子线程");
                String content = null;
                // 不断读取Socket输入流中的内容
                while (life){
                    try {
                        while (life && (content = br.readLine()) != null) {
                            // 每当读到来自服务器的数据之后，发送消息通知程序
                            // 界面显示该数据
                            Message msg = new Message();
                            msg.what = MESSAGE_RECEIVED;
                            msg.obj = content;
                            handler.sendMessage(msg);
                        }
                    } catch ( SocketTimeoutException e){
                        //每一段时间没有收到数据就会触发该异常，主要目的是确保结束已死掉的线程，从而增加一道保险。
                        Timber.e( SOCKET_TIMEOUT/1000/60 + "分钟内没有接收到来自服务器的任何数据");
                    } catch (IOException e) {
                        if( life ){
                            Timber.e("与服务器断开连接");
                            EventBus.getDefault().post(MsgBean.getInstance(MsgBean.TYPE_MODUO_TEXT, MsgBean.STATE_SENT, "与服务器 " + ip + ": " + port + " 断开连接 "));
                        }
                        e.printStackTrace();
                    }
                }
                Timber.e("接收线程已结束");
            }
        }.start();
        // 为当前线程初始化Looper
        Looper.prepare();

        initRevHandler();
        firstConnect();

        // 启动Looper
        Looper.loop();
    }

    /**
     * 初始化用于发送的Handler
     */
    private void initRevHandler() {
        // 创建revHandler对象
        revHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 接收到UI线程中用户输入的数据
                if (msg.what == MESSAGE_SEND) {
                    // 将用户在文本框内输入的内容写入网络
                    try {
                        os.write((msg.obj.toString() + "\r\n")
                                .getBytes("utf-8"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    /**
     * 首次连接时握手
     */
    private void firstConnect() {
        try {
            os.write(CONNECT_VERIFY.getBytes("utf-8"));
            Timber.e("已发送首次握手信息");
        } catch (IOException e) {
            Timber.e("首次握手失败");
            e.printStackTrace();
        }
    }

    /**
     * 将该线程的生命标志位置为 false
     * 关闭该线程的所有开启的流和 socket
     */
    public void endLife() {
        life = false;
        try {
            if( os != null){
                os.close();
                os = null;
            }
            if( br != null){
                br.close();
                br = null;
            }
            if( socket != null){
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            Timber.e("连接关闭异常");
            e.printStackTrace();
        }
        Timber.e("结束线程");
    }
}

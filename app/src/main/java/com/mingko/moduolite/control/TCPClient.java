package com.mingko.moduolite.control;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import timber.log.Timber;

/**
 * TCP 传输协议，与云魔哆连接
 *
 * Created by SunsetKnight on 2016/7/29.
 */
public class TCPClient implements Runnable {

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
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os = socket.getOutputStream();
        } catch ( UnknownHostException e) {
            Timber.e("未能识别的服务器地址");
            return;
        } catch ( ConnectException e){
            Timber.e("找不到指定端口");
            return;
        } catch ( IOException e) {
            Timber.e("服务器连接失败");
            return;
        }

        // 启动一条子线程来读取服务器响应的数据
        new Thread() {
            @Override
            public void run() {
                String content = null;
                // 不断读取Socket输入流中的内容
                try {
                    while (life && (content = br.readLine()) != null) {
                        // 每当读到来自服务器的数据之后，发送消息通知程序
                        // 界面显示该数据
                        if( life ){
                            Message msg = new Message();
                            msg.what = 0x123;
                            msg.obj = content;
                            handler.sendMessage(msg);
                        } else {
                            Timber.e(" 在收到下一行文字后结束该子线程。 ");
                        }
                    }
                    Timber.e(" 结束该子线程");
                } catch (IOException e) {
                    Timber.e("与服务器连接丢失");
                    e.printStackTrace();
                }
            }
        }.start();
        // 为当前线程初始化Looper
        Looper.prepare();

        // 创建revHandler对象
        revHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 接收到UI线程中用户输入的数据
                if (msg.what == 0x345) {
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
        // 启动Looper
        Looper.loop();

    }

    public void endLife() {
        this.life = false;
        try {
            os.close();
            br.close();
            socket.close();
        } catch (IOException e) {
            Timber.e("连接关闭异常");
            e.printStackTrace();
        }
    }
}

package com.mingko.modolite.control.tcp;

import java.io.BufferedWriter;
import java.io.IOException;

public class SendThread implements Runnable{
	
	private BufferedWriter bw ;
	private String message = null;

	public SendThread(BufferedWriter bw) {
		this.bw = bw;
	}

	public void run(){
		try {
			bw.write(message+"\n");
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
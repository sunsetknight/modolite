package com.mingko.moduolite.control.tcp;

import java.io.BufferedReader;
import java.io.IOException;

import timber.log.Timber;

public class GetThread implements Runnable{
	
	BufferedReader br ;
	
	public GetThread(BufferedReader br){
		this.br = br;
	}
	
	public void run(){
		while(true){
			try {
				Timber.e(br.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

package com.windoor.proxy;

import java.io.IOException;

public class RunMyproxy implements Runnable{
	MyProxy mp = new MyProxy();
	final int SLEEPTIME = 1000*3*10;
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			
			try {
				Thread.sleep(SLEEPTIME);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				mp.setproxy();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mp.getproxy();
		}
	}
	
}

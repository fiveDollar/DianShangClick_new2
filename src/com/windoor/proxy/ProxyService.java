package com.windoor.proxy;

import java.io.IOException;

public class ProxyService {
	public static void start(){
		MyProxy mp = new MyProxy();
		try {
			mp.setproxy();
		} catch (IOException e) {
			e.printStackTrace();
		}
		RunMyproxy rm = new RunMyproxy();
		Thread s = new Thread(rm);
		s.start();
	}
}

package com.traffic.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import com.windoor.proxy.MyProxy;
import com.windoor.proxy.ProxyService;

public class Test {

	public static void main(String[] args) {
		MyProxy mp = new MyProxy();
		ProxyService.start();
		
		mp.change();
		for(int i =0;i<1000;i++){
			mp.change();
			checkProxy(mp.host,mp.port);
		}
	}
	private static int checkProxy(String host,int port) {		
		String url = "http://www.szwindoor.com/test/test_proxy.php";
		HttpURLConnection con = null;
		BufferedReader in = null;
		try {
			con = (HttpURLConnection)new URL(url).openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port)));
			con.setConnectTimeout(300);
			con.setReadTimeout(300);
			long a = System.currentTimeMillis();
			in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
			long b = System.currentTimeMillis()-a;
			if(b<100){
				System.out.println(host+":"+port);
			}
			StringBuilder sb = new StringBuilder();
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
			}
			String numStr = sb.toString();
			return Integer.parseInt(numStr);
		} catch (NumberFormatException e) {
			return 5;
		} catch (IOException e) {
			return 4;
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
	}
}

package com.windoor.proxy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
public class MyProxy {
	ArrayList<String> proxy ;
	static String dog  = "dog2";
	public int port ;
	public String host;
//	static String proxy ;
	public  void change(){
		if(proxy == null||proxy.size()<1){
			proxy =getproxy();
		}
		int len = proxy.size();
		int random = (int) (Math.random() * len);
		proxy =getproxy();
		String[] a = null;
		while (proxy.size() <= 0) {
			proxy = getproxy();
		}
		while (a==null||a.length != 2) {
			a = proxy.get(random).split(":");
		}
		host = a[0];
		port = Integer.parseInt(a[1]);
		
	}
	public  ArrayList<String> getproxy() {
		ArrayList<String> data = new ArrayList<String>();
		File f = new File("proxy.txt");
		if(!f.exists()){
			return null;
		}
		InputStreamReader ir;
		try {
			ir = new InputStreamReader(new FileInputStream(f), "utf-8");
			BufferedReader br = new BufferedReader(ir);
			String line = null;
			while ((line = br.readLine()) != null) {
				if(line.matches("\\d*.\\d*.\\d*.\\d*:\\d*")){
					data.add(line);
				}
			}
			ir.close();
			br.close();
			return data;
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		proxy = data;
		return data;
	}
	public static void setproxy() throws IOException{
		String webcon =null;
		GetWebcon g =new GetWebcon();
		while(webcon==null||webcon.length()<100){
			System.out.println("get proxy");
			//YXD76THGFodksYU765dIUHASDFGH12345
			//107.160.10.74
			//http://107.160.10.74/wdproxy.php?sign=YXD76THGFodksYU765dIUHASDFGH12345&num=1000
			//http://192.184.40.114/wdproxy.php?sign=YXD76THGFodksYU765dIUHASDFGH12345&num=1000
			//http://116.255.179.59:808/ipl.aspx?k=2
			//http://116.255.179.59:808/api.aspx?key=2
//			webcon=	g.getWebcon("http://116.255.179.59:808/api.aspx?key=2").replaceAll("<br/>", "\r\n");
		webcon=	g.getWebcon("http://www.szwindoor.com/test/proxy.php?sign=YXD76THGFodksYU765dIUH").replaceAll("<br/>", "\r\n");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(webcon!=null)
			System.out.println("webcon len:"+webcon.length());
		else
			System.out.println("webcon is null");
//			webcon=	g.getWebcon("http://192.184.40.114/wdproxy.php?sign=YXD76THGFodksYU765dIUHASDFGH12345&num=6000").replaceAll("<br/>", "\r\n");
//			webcon=	g.getWebcon("http://116.255.179.59:808/ipl.aspx?k=2").replaceAll("<br/>", "\r\n");
		}
		File f = new File("proxy.txt");
		if(!f.exists()){
			f.createNewFile();
		}
		OutputStreamWriter ow = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
		BufferedWriter bw = new BufferedWriter(ow);
		bw.write(webcon);
		bw.flush();
		bw.close();
		ow.close();
	}
	public boolean check(){
		if(port!=0&&host!=null){
			return true;
		}else{
			return false;
		}
	}
	public String toString(){
		if(port!=0&&host!=null){
			return host +":"+ port+"";
		}else{
			return null;
		}
	}
}

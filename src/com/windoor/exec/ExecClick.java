package com.windoor.exec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import com.windoor.database.TaskDatabase;
import com.windoor.e_business.Runfactory;
import com.windoor.lancher.Lancher;
import com.windoor.parameters.Parameter;
import com.windoor.proxy.MyProxy;
import com.windoor.relate_click.RunMobile;
import com.windoor.task.Task;
import com.windoor.task.TaskKeeper;

public class ExecClick extends Thread{
	Task task;
	static int i=0;
	public ExecClick(){
		this.setName(Parameter.TaskNameHead+(++i));
	}

	@Override
	public void run() {
		while(true){
				this.task = TaskKeeper.getTask();
			if(task!=null){
				MyProxy mp = new MyProxy();
				mp.change();
				String host = mp.host;
				int port = mp.port;
				while(checkProxy(host, port)!=1){
					mp.change();	
					host = mp.host;
					port = mp.port;
				}
				Result r = Runfactory.getRun(task.type).runOne(task, mp.host, mp.port);
				synchronized (Lancher.resultList) {
					if(r.isSuccessed == 1){
						TaskDatabase tdb = new TaskDatabase();
						String sql = "update all_task set completed_task_count=completed_task_count+1 where task_id = "+task.task_id+";";
						tdb.inserAll(sql);
					}
					Lancher.resultList.add(r);
				}
			}else{
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static int checkProxy(String host,int port) {		
		String url = "http://www.szwindoor.com/test/test_proxy.php";
		HttpURLConnection con = null;
		BufferedReader in = null;
		try {
			con = (HttpURLConnection)new URL(url).openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port)));
			con.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
			con.setConnectTimeout(300);
			con.setReadTimeout(300);
			long c = System.currentTimeMillis();
			in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
			long b = System.currentTimeMillis()-c;
			
			if(b>150){
//				System.out.println("proxy time :"+b);
//				System.out.println(host+":"+port);
				return 6;
			}else{
//				System.out.println("proxy time :"+b);
//				System.out.println(host+":"+port);
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

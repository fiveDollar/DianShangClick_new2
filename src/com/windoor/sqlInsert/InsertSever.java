package com.windoor.sqlInsert;

import com.windoor.database.Database;
import com.windoor.database.Databaselinux;
import com.windoor.exec.Result;
import com.windoor.lancher.Lancher;

public class InsertSever implements Runnable{
	
	public static void start(){
		Thread s = new Thread(new InsertSever());
		s.start();
	}
	@Override
	public void run() {
		while(true){
			try {
				synchronized(Lancher.resultList){
					if(Lancher.resultList.size()>=10){
						String sql = Result.getsql(Lancher.resultList);
						Databaselinux db = new Databaselinux();
						if(sql!=null){
							db.inserAll(sql);
							System.out.println("插入成功");
						}else{
						}
						Lancher.resultList.clear();
					}
				}
			
			} catch(Exception e){
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}

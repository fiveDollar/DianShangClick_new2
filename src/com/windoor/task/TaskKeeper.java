package com.windoor.task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.windoor.count_save.Count;
import com.windoor.count_save.CountSave;
import com.windoor.database.TaskDatabase;
import com.windoor.lancher.Lancher;

public class TaskKeeper {
	public static List<Task> taskList ;
	public static List<Task> execTaskList = new ArrayList<Task>();
	static String path;
	
	public static Task getTask(){
		if(!GetNowDate().equals(Lancher.count.date)){
			System.out.println("zzzzzz");
			try {
				Thread.sleep(1000*60*5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				Runtime.getRuntime().exec("kill -s 9 `ps -aux | grep chrome | awk '{print $2}'`");
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000*60*3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Lancher.count.count=0;
			Lancher.count.date=GetNowDate();
		}
		TaskDatabase db = new TaskDatabase();
		ArrayList<Object[]> taskData = db.selectall("select * from all_task where is_exec =1 and completed_task_count < task_count ");
		Collections.shuffle(taskData);
		for(Object[] b: taskData){
			int residue = (int)b[4] - (int)b[5];
			if(residue>0){
				System.out.println((double)(int)b[5]/(double)(int)b[4]);
				System.out.println((double)current_hour()/(double)24);
				if(((double)(int)b[5]/(double)(int)b[4])>((double)current_hour()/(double)24)){
					break;
				}
			}else{
				break;
			}
			Task t = new Task();
			t.task_id = (int) b[0];
			t.keyword = (String) b[1];
			t.type =((Integer) b[2]).toString();
			t.id = (String) b[3];
			return t;
		}
		return null;
	}
	public static int count(){
		return taskList.size();
	}
	private static int current_hour(){
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		return hour;
	}
	public static String GetNowDate(){
	    String temp_str="";   
	    Date dt = new Date();   
	    //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制   
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
	    temp_str=sdf.format(dt);   
	    return temp_str;   
	} 
	
}

package com.windoor.lancher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.wd.selenium.driver.CDriverService;
import com.windoor.count_save.Count;
import com.windoor.exec.Result;
import com.windoor.proxy.ProxyService;
import com.windoor.sqlInsert.InsertSever;
import com.windoor.task.TaskKeeper;
import com.windoor.taskProtect.TaskMonitorThread;

public class Lancher {
	public static ArrayList<Result> resultList = new ArrayList<>();
	public static Count count = new Count();

	public static void main(String[] args) {
		count.date = GetNowDate();
		CDriverService.start();
		TaskKeeper tk = new TaskKeeper();
		ProxyService.start();
		TaskMonitorThread taskMonitor = new TaskMonitorThread();
		taskMonitor.start();
		InsertSever.start();
	}

	public static String GetNowDate() {
		String temp_str = "";
		Date dt = new Date();
		// 最后的aa表示“上午”或“下午” HH表示24小时制 如果换成hh表示12小时制
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		temp_str = sdf.format(dt);
		return temp_str;
	}
}
